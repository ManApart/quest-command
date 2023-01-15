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