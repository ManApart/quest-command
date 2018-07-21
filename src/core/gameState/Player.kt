package core.gameState

import system.BodyManager

class Player : Target {
    val creature = Creature("Player", "Our Hero!", BodyManager.getBody("Human"), GameState.world.findLocation(listOf("an open field")), this)

    override val name: String get() = creature.name
    override val description: String get() = creature.description
    override val properties: Properties get() = creature.properties

    init {
        creature.soul.addStat(Stat.HEALTH, 100)
        creature.soul.addStat(Stat.STAMINA, 100)
        creature.soul.addStat(Stat.STRENGTH, 1)
        creature.soul.addStat(Stat.CLIMBING, 1)
        creature.soul.addStat(Stat.AGILITY, 1)
    }

    override fun toString(): String {
        return name
    }


}