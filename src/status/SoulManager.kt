package status

import core.events.EventListener
import core.gameState.GameState
import core.gameState.Stat
import core.utility.StringFormatter
import status.RestEvent
import status.StatChangeEvent
import status.StatMaxedEvent
import status.StatMinnedEvent
import system.EventManager

object SoulManager {

    class RestHandler : EventListener<RestEvent>() {
        override fun execute(event: RestEvent) {
            EventManager.postEvent(StatChangeEvent(GameState.player, "Resting", Stat.STAMINA, event.hoursRested))
        }
    }

    class StatChangeHandler : EventListener<StatChangeEvent>() {
        override fun execute(event: StatChangeEvent) {
            val change = StringFormatter.format(event.amount > 0, "increases", "decreases")
            val subject = StringFormatter.format(event.target == GameState.player, "your", event.target.name + "'s")
            println("${event.source} $change $subject ${event.type} by ${Math.abs(event.amount)}")
            event.target.soul.incStat(event.target, event.type, event.amount)
            val current = event.target.soul.getCurrent(event.type)
            val max = event.target.soul.getTotal(event.type)
            println("$subject ${event.type} $change to $current/$max")
        }

    }

    class StatMinnedHandler : EventListener<StatMinnedEvent>() {
        override fun execute(event: StatMinnedEvent) {
            when (event.stat.toLowerCase()){
                Stat.HEALTH.toLowerCase() -> println("Oh dear, you have died!")
                Stat.STAMINA.toLowerCase() -> println("You are completely exhausted.")
            }
        }
    }

    class StatMaxedHandler : EventListener<StatMaxedEvent>() {
        override fun execute(event: StatMaxedEvent) {
            when (event.stat.toLowerCase()){
                Stat.HEALTH.toLowerCase() -> println("You feel the fullness of life beating in your bosom.")
                Stat.STAMINA.toLowerCase() -> println("You feel totally energized.")
            }
        }
    }
}