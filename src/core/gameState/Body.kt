package core.gameState

import inventory.equipItem.EquippedItemEvent
import inventory.unEquipItem.UnEquippedItemEvent
import system.EventManager

class Body(val name: String, private val parts: List<BodyPart> = listOf()) {

    fun copy(): Body {
        return Body(name, parts.map { BodyPart(it.name) })
    }

    fun hasPart(part: String): Boolean {
        return parts.firstOrNull { it.name.toLowerCase() == part.toLowerCase() } != null
    }

    private fun getPart(part: String): BodyPart {
        return parts.first { it.name.toLowerCase() == part.toLowerCase() }
    }

    private fun getPartsEquippedWith(item: Item): List<BodyPart> {
        return parts.filter { it.equippedItem == item }
    }

    fun canEquip(slot: List<String>): Boolean {
        slot.forEach { part ->
            if (!hasPart(part)) {
                return false
            }
        }
        return true
    }

    fun equip(item: Item) {
        val slot = item.equipSlots.first { canEquip(it) }
        equip(item, slot)
    }

    fun equip(item: Item, slot: List<String>) {
        slot.forEach {
            val equippedItem = getPart(it).equippedItem
            if (equippedItem != null) {
                unEquip(equippedItem)
            }
        }
        slot.forEach {
            getPart(it).equippedItem = item
        }
        //TODO - use creature, not player
        EventManager.postEvent(EquippedItemEvent(GameState.player, item))
    }

    fun unEquip(item: Item) {
        getPartsEquippedWith(item).forEach {
            it.equippedItem = null
        }
        EventManager.postEvent(UnEquippedItemEvent(GameState.player, item))
    }

}