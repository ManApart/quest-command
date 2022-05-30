package gameState

import core.properties.Properties
import core.properties.Tags
import core.properties.Values
import core.thing.Thing
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ItemTest {

    @Test
    fun canBeHeldByContainerWithProperties() {
        val item = Thing("Apple", properties = Properties(tags = Tags("Raw")))
        val properties = Properties(values = Values("CanHold" to "Raw,Food"))
        assertTrue(item.properties.canBeHeldByContainerWithProperties(properties))
    }

    @Test
    fun canBeHeldByContainerWithPropertiesEmpty() {
        val item = Thing("Apple")
        val properties = Properties()
        assertTrue(item.properties.canBeHeldByContainerWithProperties(properties))
    }

    @Test
    fun canBeHeldByContainerWithPropertiesNegative() {
        val item = Thing("Apple", properties = Properties(tags = Tags("Small")))
        val properties = Properties(values = Values("CanHold" to "Raw,Food"))
        assertFalse(item.properties.canBeHeldByContainerWithProperties(properties))
    }
}