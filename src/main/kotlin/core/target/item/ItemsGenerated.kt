package core.target.item

class ItemsGenerated : ItemsCollection {
    override val values = listOf<ItemResource>(resources.target.items.Food()).flatMap { it.values }
}