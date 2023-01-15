package validation

import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

/*
This tool should validate things the compiler doesn't know to check, like unique names in data etc.
 */
class ValidationTest {

    @Test
    fun allValidationsPass() {
        runBlocking {
            val warnings =
                ActivatorValidator().validate() +
                        CommandValidator().validate() +
                        QuestValidator().validate() +
                        DesireValidator().validate()

            assertEquals(0, warnings)
        }
    }

}