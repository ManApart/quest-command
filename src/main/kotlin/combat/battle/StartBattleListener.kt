package combat.battle

import combat.AttackEvent
import combat.battle.position.TargetDistance
import core.events.EventListener
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.body.BodyPart
import system.EventManager
import system.gameTick.GameTickEvent

class StartBattleListener : EventListener<AttackEvent>() {
    override fun execute(event: AttackEvent) {
        if (event.target is Creature && GameState.battle == null) {
            val range = event.sourcePart.getEquippedWeapon()?.getRange() ?: TargetDistance.DAGGER
            GameState.battle = Battle(listOf(event.source, event.target), range)
            GameState.player.canRest = false
            GameState.player.canTravel = false
            EventManager.postEvent(GameTickEvent())
        }
    }
}