package core.thing.item
import core.thing.ThingBuilder

class ItemsMock(override val values: List<ThingBuilder> = listOf()) : ItemsCollection {
    init {
        values.forEach { builder ->
            builder.props { tag(ITEM_TAG) }
        }
    }
}