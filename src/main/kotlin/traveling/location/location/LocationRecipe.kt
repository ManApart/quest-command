package traveling.location.location

import core.history.display
import core.properties.Properties
import core.target.Target
import core.utility.Named
import dialogue.DialogueOptions

val NOWHERE = LocationRecipe("Nowhere")

const val HEAT = "Heat"
const val LIGHT = "Light"

//TODO - combine items and equipped items
//only do equip check if slots exist
class LocationRecipe(
        override val name: String,
        private val description: DialogueOptions = DialogueOptions(""),
        val activators: List<LocationTarget> = listOf(),
        val creatures: List<LocationTarget> = listOf(),
        val items: List<LocationTarget> = listOf(),
        val weatherChangeFrequency: Int = 5,
        private val weather: DialogueOptions = DialogueOptions("Still"),
        val properties: Properties = Properties(),
        val slots: List<String> = listOf()
) : Named {
    constructor(base: LocationRecipe) : this(
            base.name,
            base.description,
            base.activators.toList(),
            base.creatures.toList(),
            base.items.toList(),
            base.weatherChangeFrequency,
            base.weather,
            Properties(base.properties),
            base.getAttachPoints()
    )

    private var equippedItems: MutableMap<String, Target?> = slots.map { it.toLowerCase() to null }.toMap().toMutableMap()

    override fun toString(): String {
        return name
    }

    fun getWeatherName() : String {
        return weather.getOption() ?: ""
    }

    fun getDescription(): String {
        return description.getOption() ?: ""
    }

    fun getAttachPoints(): List<String> {
        return equippedItems.keys.toList()
    }

    fun hasAttachPoint(attachPoint: String): Boolean {
        return equippedItems.map { it.key.toLowerCase() }.contains(attachPoint.toLowerCase())
    }

    fun getEquippedItem(slot: String): Target? {
        return equippedItems[slot.toLowerCase()]
    }

    fun getEquippedItems(): List<Target> {
        return equippedItems.values.filterNotNull()
    }

    fun getEquippedItemMap(): Map<String, Target?> {
        return equippedItems
    }

    fun getEquippedWeapon(): Target? {
        return equippedItems.values.firstOrNull { it?.properties?.tags?.has("Weapon") ?: false }
    }

    fun equipItem(attachPoint: String, item: Target) {
        if (!equippedItems.containsKey(attachPoint.toLowerCase())) {
            display("Couldn't equip $item to $attachPoint of body part $name. This should never happen!")
        } else {
            equippedItems[attachPoint.toLowerCase()] = item
        }
    }

    fun unEquip(item: Target) {
        equippedItems.keys.forEach {
            if (equippedItems[it] == item) {
                equippedItems[it] = null
            }
        }
    }

}