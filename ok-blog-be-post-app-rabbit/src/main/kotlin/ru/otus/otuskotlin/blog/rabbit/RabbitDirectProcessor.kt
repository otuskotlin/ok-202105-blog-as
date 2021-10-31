package ru.otus.otuskotlin.blog.rabbit

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.otus.otuskotlin.blog.backend.common.context.CorStatus
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.services.PostService
import ru.otus.otuskotlin.blog.backend.transport.mapping.toInitResponse
import ru.otus.otuskotlin.blog.openapi.models.BaseMessage
import java.time.Instant

class RabbitDirectProcessor(
    config: RabbitConfig,
    consumerTag: String = "",
    private val keyIn: String,
    private val keyOut: String,
    private val exchange: String,
    private val queue: String,
    private val service: PostService,
) : RabbitProcessorBase(config, consumerTag) {
    private val jacksonMapper = ObjectMapper()

    override fun Channel.getDeliveryCallback(): DeliverCallback {
        val channel = this
        return DeliverCallback { tag, message ->
            runBlocking {
                val context = PostContext(
                    startTime = Instant.now()
                )
                try {
                    val query =
                        withContext(Dispatchers.IO) { jacksonMapper.readValue(message.body, BaseMessage::class.java) }
                    val response = service.handlePost(context, query)
                    withContext(Dispatchers.IO) { jacksonMapper.writeValueAsBytes(response) }.also {
                        channel.basicPublish(exchange, keyOut, null, it)
                    }
                } catch (e: Throwable) {
                    withContext(NonCancellable) {
                        e.printStackTrace()
                        context.status = CorStatus.ERROR
                        context.addError(e = e)
                        val response = context.toInitResponse()
                        jacksonMapper.writeValueAsBytes(response).also {
                            channel.basicPublish(exchange, keyOut, null, it)
                        }
                    }
                }
            }
        }
    }

    override fun Channel.getCancelCallback() = CancelCallback {
        println("[$it] was cancelled")
    }

    override fun Channel.listen(deliverCallback: DeliverCallback, cancelCallback: CancelCallback) {
        // Объявляем обменник типа "direct" (сообщения передаются в те очереди, где ключ совпадает)
        exchangeDeclare(exchange, "direct")
        // Объявляем очередь (не сохраняется при перезагрузке сервера; неэксклюзивна - доступна другим соединениям;
        // не удаляется, если не используется)
        queueDeclare(queue, false, false, false, null)
        // связываем обменник с очередью по ключу (сообщения будут поступать в данную очередь с данного обменника при совпадении ключа)
        queueBind(queue, exchange, keyIn)
        // запуск консьюмера с автоотправкой подтверждение при получении сообщения
        basicConsume(queue, true, consumerTag, deliverCallback, cancelCallback)
        while (isOpen) {
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        println("Channel for [$consumerTag] was closed.")
    }
}
