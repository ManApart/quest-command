package combat.battle

import combat.AttackEvent
import core.events.EventListener
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Target
import system.EventManager
import system.gameTick.GameTickEvent

object StartBattle {

    class AttackListener : EventListener<AttackEvent>() {
        override fun execute(event: AttackEvent) {
            startBattle(event.source, event.target)
        }
    }

    fun startBattle(aggressor: Creature, victim: Target) {
        if (victim is Creature && GameState.battle == null){
            GameState.battle = Battle(listOf(aggressor, victim))
            GameState.player.canRest = false
            GameState.player.canTravel = false
            EventManager.postEvent(GameTickEvent())
        }
    }


}