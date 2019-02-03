package combat

import combat.battle.BattleAction
import combat.battle.position.TargetPosition
import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Target
import core.gameState.body.BodyPart

class StartAttackEvent(val source: Creature, val sourcePart: BodyPart, val target: Target, val targetPosition: TargetPosition, val type: AttackType, override var timeLeft: Int = 1) : Event, BattleAction {

    override fun getActionEvent(): AttackEvent {
        return AttackEvent(source, sourcePart, target, targetPosition, type)
    }
}
