package ru.otus.otuskotlin.blog.rabbit

import blog.stubs.PostStub
import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Test
import org.testcontainers.containers.RabbitMQContainer
import ru.otus.otuskotlin.blog.backend.services.PostService
import ru.otus.otuskotlin.blog.logics.PostCrud
import ru.otus.otuskotlin.blog.openapi.models.BaseDebugRequest
import ru.otus.otuskotlin.blog.openapi.models.CreatablePost
import ru.otus.otuskotlin.blog.openapi.models.CreatePostRequest
import ru.otus.otuskotlin.blog.openapi.models.CreatePostResponse
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class RabbitMqItTest {
    companion object {
        const val keyIn = "key-in"
        const val keyOut = "key-out"
        const val exchange = "post-exchange"
        private const val queue = "post-queue"
        private val container by lazy {
            RabbitMQContainer("rabbitmq:latest").apply {

                withUser("guest", "guest")
                start()
            }
        }
        private val rabbitMqTestPort: Int by lazy {
            container.getMappedPort(5672)
        }
        val config by lazy {
            RabbitConfig(
                port = rabbitMqTestPort
            )
        }
        private val crud = PostCrud()
        private val service = PostService(crud)
        private val processor by lazy {
            RabbitDirectProcessor(
                config = config,
                keyIn = keyIn,
                keyOut = keyOut,
                exchange = exchange,
                queue = queue,
                service = service,
                consumerTag = "post-tag"
            )
        }
        val controller by lazy {
            RabbitController(
                processors = setOf(processor)
            )
        }
        val mapper = ObjectMapper()
    }

    @BeforeTest
    fun tearUp() {
        controller.start()
    }

    @Test
    fun postCreateTest() {
        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.getBody(), Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, mapper.writeValueAsBytes(postCreate))

                runBlocking {
                    withTimeoutOrNull(250L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = mapper.readValue(responseJson, CreatePostResponse::class.java)
                val expected = PostStub.getModel()
                assertEquals(expected.title, response.createdPost?.title)
                assertEquals(expected.content, response.createdPost?.content)
            }
        }
    }

    private val postCreate = with(PostStub.getModel()) {
        CreatePostRequest(
            creatablePost = CreatablePost(
                title = title,
                content = content
            ),
            debug = BaseDebugRequest(
                mode = BaseDebugRequest.Mode.STUB,
                stubCase = BaseDebugRequest.StubCase.SUCCESS,
            )
        )
    }
}
