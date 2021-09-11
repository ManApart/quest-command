package core.utility

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test
import traveling.location.location.LocationRecipe
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class JsonDirectoryParserTest {

    @Test
    fun multipleJsonFilesAreParsedAndAppendedToList() {
        val locations = NameSearchableList(JsonDirectoryParser.parseDirectory("/test/core/utility/locations", ::parseFile))

        assertEquals(3, locations.size)

        val kanbara = locations.getOrNull("Kanbara")
        val gate = locations.getOrNull("Kanbara Gate")
        val city = locations.getOrNull("Kanbara City")

        assertNotNull(kanbara)
        assertNotNull(city)
        assertNotNull(gate)


        assertEquals("The bustling port town of Kanbara is one of the most densely packed cities in all of Lenovia", city.getDescription())
        assertEquals("The bustling port town of Kanbara is one of the most densely packed cities in all of Lenovia", kanbara.getDescription())
        assertEquals(1, gate.activators.size)

    }

    private fun parseFile(path: String): List<LocationRecipe> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))
}