package processing

import events.*
import gameState.GameState
import gameState.Stat

object SoulManager {

    class RestHandler : EventListener<RestEvent>() {
        override fun execute(event: RestEvent) {
            EventManager.postEvent(StatChangeEvent(GameState.player, "Resting", Stat.StatType.STAMINA, event.hoursRested))
        }
    }

    class StatChangeHandler : EventListener<StatChangeEvent>() {
        override fun execute(event: StatChangeEvent) {
            val change = if (event.amount > 0) "increases" else "decreases"
            println("${event.source} $change your ${event.type} by ${Math.abs(event.amount)}")
            event.target.soul.incStat(event.type, event.amount)
        }

    }

    class StatMinnedHandler : EventListener<StatMinnedEvent>() {
        override fun execute(event: StatMinnedEvent) {
            when (event.stat){
                Stat.StatType.HEALTH -> println("Oh dear, you have died!")
                Stat.StatType.STAMINA -> println("You are completely exhausted.")
            }
        }
    }

    class StatMaxedHandler : EventListener<StatMaxedEvent>() {
        override fun execute(event: StatMaxedEvent) {
            when (event.stat){
                Stat.StatType.HEALTH -> println("You feel the fullness of life beating in your bosom.")
                Stat.StatType.STAMINA -> println("You feel totally energized.")
            }
        }
    }
}