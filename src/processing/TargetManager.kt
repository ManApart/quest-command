package processing

import events.Event
import events.EventListener
import gameState.Target

object TargetManager {
    class EventHandler : EventListener<Event>() {
        override fun execute(event: Event) {
//            println("Event: $event")
        }
    }

    fun targetsToString(targets: List<Target>) : String {
        val targetCounts = HashMap<String, Int>()
        targets.forEach {
            targetCounts[it.name] = targetCounts[it.name]?.plus(1) ?: 1
        }

       return targetCounts.entries.joinToString(", ") {
            if (it.value == 1) {
                it.key
            } else {
                "${it.value}x ${it.key}"
            }
        }
    }
}