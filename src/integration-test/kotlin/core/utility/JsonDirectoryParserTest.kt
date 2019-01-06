package core.utility

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.body.Body
import core.gameState.body.ProtoBody
import org.junit.Test
import kotlin.test.assertEquals

class JsonDirectoryParserTest {

    @Test
    fun multipleJsonFilesAreParsedAndAppendedToList() {
        val bodies = JsonDirectoryParser.parseDirectory("/test/core/utility/bodies", ::parseFile)

        assertEquals(2, bodies.size)
        assertEquals("FirstBody", bodies[0].name)
        assert(bodies[0].parts.contains("Head"))
        assert(bodies[0].parts.contains("Body"))
        assert(bodies[0].parts.contains("Claws"))

        assertEquals("SecondBody", bodies[1].name)
        assert(bodies[1].parts.contains("Goo"))
        assert(bodies[1].parts.contains("Antenna"))
    }

    private fun parseFile(path: String): List<ProtoBody> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))
}