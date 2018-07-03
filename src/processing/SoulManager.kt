package processing

import EventManager
import events.*
import gameState.GameState
import gameState.Stat

class SoulManager {
    init {
        RestHandler()
        StatChangeHandler()
        StatMinnedHandler()
        StatMaxedHandler()
    }

    class RestHandler : EventListener<RestEvent> {
        init {
            EventManager.registerListener(this)
        }

        override fun handle(event: RestEvent) {
            EventManager.postEvent(StatChangeEvent("Resting", Stat.StatType.STAMINA, event.hoursRested))
        }

        override fun getPriority(): Int {
            return 0
        }
    }

    class StatChangeHandler : EventListener<StatChangeEvent> {
        init {
            EventManager.registerListener(this)
        }

        override fun handle(event: StatChangeEvent) {
            val change = if (event.amount > 0) "increases" else "decreases"
            println("${event.source} $change your ${event.type} by ${Math.abs(event.amount)}")
            GameState.player.soul.incStat(event.type, event.amount)
        }

        override fun getPriority(): Int {
            return 0
        }
    }

    class StatMinnedHandler : EventListener<StatMinnedEvent> {
        init {
            EventManager.registerListener(this)
        }

        override fun handle(event: StatMinnedEvent) {
            when (event.stat){
                Stat.StatType.HEALTH -> println("Oh dear, you have died!")
                Stat.StatType.STAMINA -> println("You are completely exhausted.")
            }
        }

        override fun getPriority(): Int {
            return 0
        }
    }

    class StatMaxedHandler : EventListener<StatMaxedEvent> {
        init {
            EventManager.registerListener(this)
        }

        override fun handle(event: StatMaxedEvent) {
            when (event.stat){
                Stat.StatType.HEALTH -> println("You feel the fullness of life beating in your bosom.")
                Stat.StatType.STAMINA -> println("You feel totally energized.")
            }
        }

        override fun getPriority(): Int {
            return 0
        }
    }
}