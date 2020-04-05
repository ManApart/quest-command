package core.utility

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class MultiObjectParserTest {

    @Test
    fun parseMultiObject() {
        val mapper = jacksonObjectMapper()

        val parsed: ParentObject = mapper.readValue(File("./src/test-integration/resource/test/core/utility/MultiObjectTest.json"))
        assertEquals("Parent", parsed.name)
        assertEquals(3, parsed.children.size)
        assertEquals("Child1", parsed.children[0].id)
        assertEquals("Child2", parsed.children[1].id)
    }
}