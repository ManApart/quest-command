package status

import core.events.EventListener
import core.history.displayToMe

class LevelUp : EventListener<LevelUpEvent>() {
    override fun shouldExecute(event: LevelUpEvent): Boolean {
        return event.source.isPlayer()
    }

    override fun execute(event: LevelUpEvent) {
        event.source.displayToMe("${event.leveledStat.name} leveled up to ${event.level}.")
    }
}