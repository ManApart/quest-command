package status

import core.events.EventListener
import core.gameState.GameState
import core.gameState.getCreature
import core.gameState.isPlayer
import core.history.display

class LevelUp : EventListener<LevelUpEvent>() {
    override fun shouldExecute(event: LevelUpEvent): Boolean {
        return event.source.isPlayer()
    }

    override fun execute(event: LevelUpEvent) {
        display("${event.stat.name} leveled up to ${event.level}.")
    }
}