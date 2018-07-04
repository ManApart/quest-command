package processing.use

import events.StatChangeEvent
import events.UseItemEvent
import gameState.Creature
import gameState.Stat
import processing.EventManager

class DamageCreature : Use {
    override fun matches(event: UseItemEvent): Boolean {
        return event.target is Creature && event.source.tags.has("Weapon")
    }

    override fun execute(event: UseItemEvent) {
        val amount = if (event.source.properties.hasProperty("Damage")){
            -event.source.properties.getInt("Damage")
        } else {
            -1
        }
        EventManager.postEvent(StatChangeEvent(event.target as Creature, event.source.name, Stat.StatType.HEALTH, amount))
    }
}