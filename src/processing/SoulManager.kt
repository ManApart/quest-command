package processing

import events.*
import gameState.GameState
import gameState.Stat

object SoulManager {

    class RestHandler : EventListener<RestEvent>() {
        override fun handle(event: RestEvent) {
            EventManager.postEvent(StatChangeEvent("Resting", Stat.StatType.STAMINA, event.hoursRested))
        }
    }

    class StatChangeHandler : EventListener<StatChangeEvent>() {
        override fun handle(event: StatChangeEvent) {
            val change = if (event.amount > 0) "increases" else "decreases"
            println("${event.source} $change your ${event.type} by ${Math.abs(event.amount)}")
            GameState.player.soul.incStat(event.type, event.amount)
        }

    }

    class StatMinnedHandler : EventListener<StatMinnedEvent>() {
        override fun handle(event: StatMinnedEvent) {
            when (event.stat){
                Stat.StatType.HEALTH -> println("Oh dear, you have died!")
                Stat.StatType.STAMINA -> println("You are completely exhausted.")
            }
        }
    }

    class StatMaxedHandler : EventListener<StatMaxedEvent>() {
        override fun handle(event: StatMaxedEvent) {
            when (event.stat){
                Stat.StatType.HEALTH -> println("You feel the fullness of life beating in your bosom.")
                Stat.StatType.STAMINA -> println("You feel totally energized.")
            }
        }
    }
}