package core.utility

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Body
import org.junit.Test
import kotlin.test.assertEquals

class JsonDirectoryParserTest {

    @Test
    fun multipleJsonFilesAreParsedAndAppendedToList() {
        val bodies = JsonDirectoryParser.parseDirectory("/data/content", ::parseFile)

        assertEquals(2, bodies.size)
        assertEquals("FirstBody", bodies[0].name)
        assert(bodies[0].hasPart("Head"))
        assert(bodies[0].hasPart("Body"))
        assert(bodies[0].hasPart("Claws"))

        assertEquals("SecondBody", bodies[1].name)
        assert(bodies[1].hasPart("Goo"))
        assert(bodies[1].hasPart("Antenna"))
    }

    private fun parseFile(path: String): List<Body> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))
}