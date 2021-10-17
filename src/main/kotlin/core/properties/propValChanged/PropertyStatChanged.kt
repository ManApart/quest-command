package core.properties.propValChanged

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.utility.then
import core.utility.asSubjectPossessive

class PropertyStatChanged : EventListener<PropertyStatChangeEvent>() {

    override fun shouldExecute(event: PropertyStatChangeEvent): Boolean {
        return event.amount != 0
    }

    override fun execute(event: PropertyStatChangeEvent) {
        val change = (event.amount > 0).then("increases", "decreases")
        val values = event.thing.properties.values
        val beforeVal = values.getInt(event.statName)

        values.inc(event.statName, event.amount)

        val current = values.getInt(event.statName)
        if (!event.silent && beforeVal != current) {
            event.thing.display { listener ->
                val subject = event.thing.asSubjectPossessive(listener)
                "${event.sourceOfChange} $change $subject ${event.statName} from $beforeVal to $current."
            }
        }

        if (beforeVal > 0 && current <= 0) {
            EventManager.postEvent(PropertyStatMinnedEvent(event.thing, event.statName))
        }
    }


}