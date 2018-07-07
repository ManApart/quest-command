package use.actions

import status.StatChangeEvent
import use.UseItemEvent
import core.gameState.Creature
import core.gameState.Stat
import system.EventManager

class DamageCreature : Action {
    override fun matches(event: UseItemEvent): Boolean {
        return event.target is Creature && event.source.tags.has("Weapon")
    }

    override fun execute(event: UseItemEvent) {
        EventManager.postEvent(StatChangeEvent(event.target as Creature, event.source.name, Stat.StatType.HEALTH, -event.source.properties.getInt("Damage", 1)))
    }
}