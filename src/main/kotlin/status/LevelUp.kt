package status

import core.events.EventListener
import core.gameState.GameState
import core.history.display

class LevelUp : EventListener<LevelUpEvent>() {
    override fun shouldExecute(event: LevelUpEvent): Boolean {
        return event.creature == GameState.player.creature
    }

    override fun execute(event: LevelUpEvent) {
        display("${event.stat.name} leveled up to ${event.level}.")
    }
}