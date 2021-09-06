import com.fasterxml.jackson.databind.ObjectMapper
import ru.otus.otuskotlin.blog.openapi.models.BaseMessage
import ru.otus.otuskotlin.blog.openapi.models.CreatablePost
import ru.otus.otuskotlin.blog.openapi.models.CreatePostRequest
import ru.otus.otuskotlin.blog.openapi.models.PostStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SerializationTest {
    private val requestId = "42"
    val createRequest = CreatePostRequest(
        requestId = requestId,
        createPost = CreatablePost(
            title = "This is header",
            content = "This is text. Do u see it?",
            ownerId = "111",
            status = PostStatus.DRAFT
        )
    )
    private val om = ObjectMapper()

    @Test
    fun serializationTest() {
        val json = om.writeValueAsString(createRequest)
        println(json)
        assertTrue("json must contain discriminator") {
            json.contains(""""messageType":"${createRequest::class.simpleName}"""")
        }
        assertTrue("json must serialize status field") {
            json.contains(""""status":"${PostStatus.DRAFT.value}"""")
        }
        assertTrue("json must serialize messageId field") {
            json.contains(""""requestId":"$requestId"""")
        }
    }

    @Test
    fun deserializeTest() {
        val json = om.writeValueAsString(createRequest)
        val deserialized = om.readValue(json, BaseMessage::class.java) as CreatePostRequest

        assertEquals("This is header", deserialized.createPost?.title)
        assertEquals(requestId, deserialized.requestId)
    }
}
