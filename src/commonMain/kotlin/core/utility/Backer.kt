package core.utility

class Backer<T>(private val initializer: suspend () -> T) {
    private var backing: T? = null

    suspend fun get(): T {
        return backing ?: initializer().also { backing = it }
    }

    suspend fun reset() {
        backing = initializer()
    }
}

//Allows for applying when the lambda needs to be a suspend
suspend inline fun <T> T.applySuspending(block: suspend T.() -> Unit): T {
    return this.also { it.block() }
}