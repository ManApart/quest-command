import kotlinx.coroutines.delay
import kotlin.js.Promise

object FakeSync {

    suspend fun <T> promise(lambda: () -> Promise<T>): T {
        var done = false
        var result: T? = null
        lambda().then { res ->
            result = res
            done = true
        }
        while (!done){
            delay(100)
        }

        return result!!
    }
}