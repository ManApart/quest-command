package core.properties

import org.junit.Test
import kotlin.test.assertEquals

class ValuesTest {

    @Test
    fun putPropertyCreatesIt(){
        val values = Values()
        values.put("int", 1)
        values.put("bool", true)
        values.put("string", "value")

        assertEquals(1, values.getInt("int"))
        assertEquals(true, values.getBoolean("bool"))
        assertEquals("value", values.getString("string"))
    }

    @Test
    fun increasingAPropertyCreatesIt(){
        val values = Values()
        values.inc("int", 1)

        assertEquals(1, values.getInt("int"))
    }

    @Test
    fun increasingAPropertyUpdatesIt(){
        val values = Values()
        values.put("int", 1)
        values.inc("int", 2)

        assertEquals(3, values.getInt("int"))
    }
}