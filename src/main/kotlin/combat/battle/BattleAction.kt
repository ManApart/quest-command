package combat.battle

import combat.battle.position.TargetAim
import core.events.Event
import core.gameState.Target

interface BattleAction {
    var timeLeft: Int
    val source: Target?
    val target: TargetAim?
    fun getActionEvent(): Event
}