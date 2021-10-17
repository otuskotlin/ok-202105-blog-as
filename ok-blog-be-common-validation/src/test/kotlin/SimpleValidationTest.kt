import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.blog.common.cor.handlers.chain
import ru.otus.otuskotlin.blog.validation.IValidationError
import ru.otus.otuskotlin.blog.validation.ValidationResult
import ru.otus.otuskotlin.blog.validation.cor.workers.validation
import ru.otus.otuskotlin.blog.validation.validators.ValidatorStringNonEmpty
import kotlin.test.assertEquals

class SimpleValidationTest {
    @Test
    fun pipelineValidation() {
        val chain = chain<TestContext> {
            validation {
                errorHandler { v: ValidationResult ->
                    if (!v.isSuccess) {
                        errors.addAll(v.errors)
                    }
                }
                validate<String?> { validator(ValidatorStringNonEmpty()); on { x } }
                validate<String?> { validator(ValidatorStringNonEmpty()); on { y } }
            }
        }

        val c = TestContext()

        runBlocking {
            chain.build().exec(c)
            assertEquals(2, c.errors.size)
        }
    }

    data class TestContext(
        val x: String = "",
        val y: String = "",
        val errors: MutableList<IValidationError> = mutableListOf()
    )
}
