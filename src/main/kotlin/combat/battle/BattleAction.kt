package combat.battle

import core.events.Event

interface BattleAction {
    var timeLeft: Int
    fun getActionEvent(): Event
}