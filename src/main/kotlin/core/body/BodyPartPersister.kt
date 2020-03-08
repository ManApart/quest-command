package core.body

import inventory.Inventory
import traveling.location.location.Location

fun getPersisted(dataObject: Location): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["equippedItemNames"] = dataObject.getEquippedItemMap().mapValues { (_, value) -> value?.name }

    return data
}

@Suppress("UNCHECKED_CAST")
fun readBodyPartFromData(data: Map<String, Any>, inventory: Inventory?): Location {
    val name = data["name"] as String
    val equippedItems = (data["equippedItemNames"] as Map<String, String?>).mapValues { (_, itemName) ->
        if (itemName == null) {
            null
        } else {
            inventory?.getItem(itemName)
        }
    }

    val part = Location(name, slots = equippedItems.keys.toList())
    equippedItems.filterValues { it != null }.forEach { (attachPoint, item) ->
        part.equipItem(attachPoint, item!!)
    }
    return part
}