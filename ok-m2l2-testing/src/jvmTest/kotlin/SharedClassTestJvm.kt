import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SharedClassTestJvm {

    @Test
    fun sharedClass() = runBlocking<Unit> {
        val sc = SharedClass()
        val res = sc.request("one")
        assertEquals("Jvm done", res)
    }
}
