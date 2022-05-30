package core.thing.item

class ItemsGenerated : ItemsCollection {
    override val values by lazy { listOf<ItemResource>(resources.thing.items.Food(), resources.thing.items.Items(), resources.thing.items.Weapons(), resources.thing.items.apparel.Apparel(), resources.thing.items.apparel.IronArmor()).flatMap { it.values }}
}