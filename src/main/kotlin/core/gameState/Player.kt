package core.gameState

import crafting.Recipe
import core.gameState.stat.Stat
import core.utility.NameSearchableList
import system.BodyManager

class Player : Target {
    val creature = Creature("Player", "Our Hero!", BodyManager.getBody("Human"), GameState.world.findLocation(listOf("an open field")), null, this)

    override val name: String get() = creature.name
    override val description: String get() = creature.description
    override val properties: Properties get() = creature.properties
    var canRest = true
    var canTravel = true
    var canInteract = true
    val knownRecipes = NameSearchableList<Recipe>()

    init {
        creature.soul.addStat(Stat.HEALTH, 1, 10, 1)
        creature.soul.addStat(Stat.STAMINA, 1, 100, 1)
        creature.soul.addStat(Stat.STRENGTH, 1, 1, 1)
        creature.soul.addStat(Stat.CLIMBING, 1)
        creature.soul.addStat(Stat.AGILITY, 1)
        creature.soul.addStat(Stat.COOKING, 1)
    }

    override fun toString(): String {
        return name
    }


}