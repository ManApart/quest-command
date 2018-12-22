package validation

import org.junit.Test
import kotlin.test.assertEquals

/*
This tool should validate things the compiler doesn't know to check, like unique names in data etc.
 */
class ValidationTest {

    @Test
    fun allValidationsPass() {
        val warnings =
                ActivatorValidator().validate() +
                CommandValidator().validate() +
                CreatureValidator().validate() +
                LocationValidator().validate()

        assertEquals(0, warnings)
    }

}