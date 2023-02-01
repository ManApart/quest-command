package core.thing.item

import core.thing.ThingBuilder

class ItemsMock(val values: List<ThingBuilder> = listOf()) : ItemsCollection {
    init {
        values.forEach { builder ->
            builder.props { tag(ITEM_TAG) }
        }
    }

    override suspend fun values() = values
}