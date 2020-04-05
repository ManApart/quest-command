import core.properties.Properties
import core.properties.Tags
import core.properties.Values
import core.target.Target
import core.target.item.ITEM_TAG

fun createItem(name: String = "Apple", weight: Int = 1): Target {
    return Target(name, properties = Properties(
            Values(mapOf("weight" to weight.toString())),
            Tags(listOf(ITEM_TAG))
    ))
}

//Pouch is a container that is also an item
fun createPouch(size: Int = 5, weight: Int = 1): Target {
    return Target("Pouch", properties = Properties(
            Values(mapOf(
                    "size" to size.toString(),
                    "weight" to weight.toString()
            )),
            Tags(listOf(ITEM_TAG, "Container", "Open"))
    ))
}

// Chest is a container
fun createChest(size: Int = 10): Target {
    return Target("Chest", properties = Properties(
            Values(mapOf("size" to size.toString())),
            Tags(listOf("Container", "Open"))
    ))
}

fun createClosedChest(size: Int = 10): Target {
    return Target("Closed Chest", properties = Properties(
            Values(mapOf("size" to size.toString())),
            Tags(listOf("Container"))
    ))
}
