package combat.battle

import core.events.Event
import core.gameState.Target

interface BattleAction {
    var timeLeft: Int
    val source: Target?
    val actionTarget: Target?
    fun getActionEvent(): Event
}