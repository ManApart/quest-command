package core.gameState

import core.events.Event
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties

class TriggerCondition(private val callingEvent: String, private val eventParams: Map<String, String>) {

    fun matches(event: Event): Boolean {
        return classMatches(event) && eventValuesMatch(event)
    }

    private fun classMatches(event: Event) = event::class.simpleName == callingEvent

    private fun eventValuesMatch(event: Event) : Boolean {
        eventParams.forEach{ entry ->
            val property = event.javaClass.kotlin.memberProperties.first { it.name == entry.key }
            val expected = when {
                property.returnType.isSubtypeOf(Boolean::class.createType()) -> entry.value.toBoolean()
                property.returnType.isSubtypeOf(Int::class.createType()) -> entry.value.toInt()
                else -> entry.value
            }

            if (expected != property.get(event)){
               return false
            }
        }
        return true
    }
}