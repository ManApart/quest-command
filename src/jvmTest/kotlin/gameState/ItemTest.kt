package gameState

import core.properties.Properties
import core.properties.TagStrings.SMALL
import core.properties.Tags
import core.properties.ValueStrings.CAN_HOLD
import core.properties.Values
import core.thing.Thing
import kotlin.test.Test


import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ItemTest {

    @Test
    fun canBeHeldByContainerWithProperties() {
        val item = Thing("Apple", properties = Properties(tags = Tags("Raw")))
        val properties = Properties(values = Values(CAN_HOLD to "Raw,Food"))
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
        val item = Thing("Apple", properties = Properties(tags = Tags(SMALL)))
        val properties = Properties(values = Values(CAN_HOLD to "Raw,Food"))
        assertFalse(item.properties.canBeHeldByContainerWithProperties(properties))
    }
}
