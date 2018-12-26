package core.utility

import org.junit.Test
import kotlin.test.assertEquals

class StringHelperTest {

    @Test
    fun caseInsensitive() {
        val base = "\$burnhealth"
        val params = mapOf("burnHealth" to "1")

        assertEquals("1", base.apply(params))
    }
}