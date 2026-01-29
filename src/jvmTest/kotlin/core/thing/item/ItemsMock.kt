package core.thing.item

import core.properties.TagKey.ITEM
import core.thing.ThingBuilder

class ItemsMock(val values: List<ThingBuilder> = listOf()) : ItemsCollection {
    init {
        values.forEach { builder ->
            builder.props { tag(ITEM) }
        }
    }

    override suspend fun values() = values
}