package traveling.location.location

import core.properties.getPersisted
import inventory.Inventory
import traveling.location.Network

//TODO - Locations were immutable, but now store equipped items and properties
fun getPersisted(dataObject: Location): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["properties"] = getPersisted(dataObject.properties)
    data["equippedItemNames"] = dataObject.getEquippedItemMap().mapValues { (_, value) -> value?.name }
    return data
}

@Suppress("UNCHECKED_CAST")
fun applyFromData(data: Map<String, Any>, network: Network, inventory: Inventory?) {
    val name = data["name"] as String
    val properties = core.properties.readFromData(data["properties"] as Map<String, Any>)

    val equippedItems = (data["equippedItemNames"] as Map<String, String?>).mapValues { (_, itemName) ->
        if (itemName == null) {
            null
        } else {
            inventory?.getItem(itemName)
        }
    }
    val location = network.getLocation(name)
//    val part = Location(name, slots = equippedItems.keys.toList())
    equippedItems.filterValues { it != null }.forEach { (attachPoint, item) ->
        location.equipItem(attachPoint, item!!)
    }
    location.properties.replaceWith(properties)
}