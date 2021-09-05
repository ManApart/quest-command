package core.target.item
import core.target.TargetBuilder

class ItemsMock(override val values: List<TargetBuilder> = listOf()) : ItemsCollection {
    init {
        values.forEach { builder ->
            builder.props { tag(ITEM_TAG) }
        }
    }
}