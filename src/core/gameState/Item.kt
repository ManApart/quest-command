package core.gameState

class Item(override val name: String, override val description: String ="", val weight: Int = 0, val equipSlots: List<List<String>> = listOf(), override val properties: Properties = Properties()) : Target {
    override fun toString(): String {
        return name
    }

    fun copy() : Item {
        return Item(name, description, weight, equipSlots, properties)
    }

    fun canEquipTo(body: Body) : Boolean {
        equipSlots.forEach { slot ->
           if (body.canEquip(slot)){
               return true
           }
        }
        return false
    }
}