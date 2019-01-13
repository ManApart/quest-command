package core.gamestate

import core.gameState.Item
import core.gameState.Properties
import core.gameState.Tags
import core.gameState.Values
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ItemTest {

    @Test
    fun canBeHeldByContainerWithProperties() {
        val item = Item("Apple", properties = Properties(tags = Tags(listOf("Raw"))))
        val properties = Properties(values = Values(mapOf("CanHold" to "Raw,Food")))
        assertTrue(item.canBeHeldByContainerWithProperties(properties))
    }

    @Test
    fun canBeHeldByContainerWithPropertiesEmpty() {
        val item = Item("Apple")
        val properties = Properties()
        assertTrue(item.canBeHeldByContainerWithProperties(properties))
    }

    @Test
    fun canBeHeldByContainerWithPropertiesNegative() {
        val item = Item("Apple", properties = Properties(tags = Tags(listOf("Small"))))
        val properties = Properties(values = Values(mapOf("CanHold" to "Raw,Food")))
        assertFalse(item.canBeHeldByContainerWithProperties(properties))
    }
}