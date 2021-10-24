import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorChainDsl
import ru.otus.otuskotlin.blog.common.cor.handlers.chain
import ru.otus.otuskotlin.blog.common.cor.handlers.parallel
import ru.otus.otuskotlin.blog.common.cor.handlers.worker
import kotlin.test.Test
import kotlin.test.assertEquals

class CorTest {

    companion object {
        val chain = chain<TestContext> {
            worker {
                title = "Инициализация статуса"
                description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

                on { status == CorStatuses.NONE }
                handle { status = CorStatuses.RUNNING }
                except { status = CorStatuses.ERROR }
            }

            ru.otus.otuskotlin.blog.common.cor.handlers.chain<TestContext> {
                on { status == CorStatuses.RUNNING }

                worker(
                    title = "Лямбда обработчик",
                    description = "Пример использования обработчика в виде лямбды"
                ) {
                    some += 4
                }
            }

            parallel {
                on {
                    some < 15
                }

                worker(title = "Increment some") {
                    some++
                }
            }
            printResult()
        }.build()
    }

    @Test
    fun corTest() {
        val chain = CorTest.chain
        val ctx = TestContext(some = 13)

        runBlocking { chain.exec(ctx) }

        assertEquals(14, ctx.some)
    }
}

private fun ICorChainDsl<TestContext>.printResult() =
    worker(title = "Print example") {
        println("some = $some")
    }

data class TestContext(
    var status: CorStatuses = CorStatuses.NONE,
    var some: Int = Int.MIN_VALUE
)

enum class CorStatuses {
    NONE,
    RUNNING,
    FAILING,
    DONE,
    ERROR
}
