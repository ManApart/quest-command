package validation

import org.junit.Test
import test.validation.validate
import kotlin.test.assertEquals

class Validations {

    @Test
    fun allValidationsPass() {
        assertEquals(0, validate())
    }

}