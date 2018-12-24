package core.utility

import org.junit.Test

class ParamApplierTest {

    @Test
    fun topLevelStringReplaced() {
        val applied = ParamApplied("\$test")
        val params = mapOf("\$test" to "value")
        val result = ParamApplier.apply(applied, params)
    }
}