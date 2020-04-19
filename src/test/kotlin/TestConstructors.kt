import core.properties.*
import core.target.Target
import core.target.activator.ACTIVATOR_TAG
import core.target.creature.CREATURE_TAG
import core.target.item.ITEM_TAG
import inventory.createInventoryBody
import status.stat.STRENGTH

fun createItem(name: String = "Apple", weight: Int = 1): Target {
    return Target(name, properties = Properties(
            Values(mapOf("weight" to weight.toString())),
            Tags(listOf(ITEM_TAG))
    ))
}

//Pouch is a container that is also an item
fun createPouch(size: Int = 5, weight: Int = 1): Target {
    return Target("Pouch",
            body = createInventoryBody("Pouch", size),
            properties = Properties(
                    Values(mapOf(
                            SIZE to size.toString(),
                            WEIGHT to weight.toString()
                    )),
                    Tags(listOf(ITEM_TAG, CONTAINER, OPEN, ITEM_TAG))
            ))
}

// Chest is a container
fun createChest(size: Int = 10): Target {
    return Target("Chest", body = createInventoryBody("Chest", size),
            properties = Properties(
                    Values(mapOf(SIZE to size.toString())),
                    Tags(listOf(CONTAINER, OPEN, ACTIVATOR_TAG))
            ))
}

fun createClosedChest(size: Int = 10): Target {
    return Target("Closed Chest",
            body = createInventoryBody("Closed Chest", size),
            properties = Properties(
                    Values(mapOf(SIZE to size.toString())),
                    Tags(listOf(CONTAINER, ACTIVATOR_TAG))
            ))
}

fun createPackMule(strength: Int = 1): Target {
    return Target("Pack Mule", body = createInventoryBody("Pack Mule"),
            properties = Properties(
                    Values(mapOf(STRENGTH to strength.toString())),
                    Tags(listOf(CONTAINER, OPEN, CREATURE_TAG))
            ))
}
