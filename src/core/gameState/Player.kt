package core.gameState

import core.gameState.stat.Stat
import system.BodyManager

class Player : Target {
    val creature = Creature("Player", "Our Hero!", BodyManager.getBody("Human"), GameState.world.findLocation(listOf("an open field")), null, this)

    override val name: String get() = creature.name
    override val description: String get() = creature.description
    override val properties: Properties get() = creature.properties
    var canRest = true

    init {
        creature.soul.addStat(Stat.HEALTH, 1, 10, 1)
        creature.soul.addStat(Stat.STAMINA, 1, 100, 1)
        creature.soul.addStat(Stat.STRENGTH, 1, 1, 1)
        creature.soul.addStat(Stat.CLIMBING, 1)
        creature.soul.addStat(Stat.AGILITY, 1)
    }

    override fun toString(): String {
        return name
    }


}