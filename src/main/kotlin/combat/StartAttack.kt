package combat

import combat.battle.Battle
import combat.battle.position.TargetDistance
import core.events.EventListener
import core.gameState.Creature
import core.gameState.GameState
import system.EventManager
import system.gameTick.GameTickEvent

class StartAttack : EventListener<StartAttackEvent>() {
    override fun execute(event: StartAttackEvent) {
        if (event.target is Creature) {
            if (GameState.battle == null) {
                val range = event.sourcePart.getEquippedWeapon()?.getRange() ?: TargetDistance.DAGGER
                GameState.battle = Battle(listOf(event.source, event.target), range)
                GameState.player.canRest = false
                GameState.player.canTravel = false
                EventManager.postEvent(GameTickEvent())
            } else {
                GameState.battle?.addAction(event.source, event)
            }
        }
    }


}