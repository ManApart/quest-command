package core.utility

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

//TODO - delete this test and the tested classes
class MultiObjectParserTest {

    @Test
    fun parseMultiObject() {
        val mapper = jacksonObjectMapper()

        val parsed: ParentObject = mapper.readValue(File("./src/integration-test/resource/test/core/utility/MultiObjectTest.json"))
        assertEquals("Parent", parsed.name)
        assertEquals(3, parsed.children.size)
        assertEquals("Child1", parsed.children[0].id)
        assertEquals("Child2", parsed.children[1].id)
    }
}