package core.target.item

class ItemsGenerated : ItemsCollection {
    override val values = listOf<ItemResource>(resources.target.items.Food(), resources.target.items.Items(), resources.target.items.Weapons(), resources.target.items.apparel.Apparel(), resources.target.items.apparel.IronArmor()).flatMap { it.values }
}