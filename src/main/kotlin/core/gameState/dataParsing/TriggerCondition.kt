package core.gameState.dataParsing

import core.events.Event
import core.utility.Named
import core.utility.apply
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties

class TriggerCondition(val callingEvent: String, private val eventParams: Map<String, String> = mapOf(), private val queries: List<Query> = listOf()) {

    fun applyParamValues(paramValues: Map<String, String>): TriggerCondition {
        val modifiedParams = eventParams.apply(paramValues)
        return TriggerCondition(callingEvent, modifiedParams)
    }

    fun matches(event: Event): Boolean {
        val params = getEventValues(event)
        return classMatches(event) && eventValuesMatch(params) && queries.all { it.evaluate(params) }
    }

    private fun classMatches(event: Event) = event::class.simpleName == callingEvent

    private fun getEventValues(event: Event): Map<String, String>{
        val values = mutableMapOf<String, String>()

        event.javaClass.kotlin.memberProperties.forEach { property ->
            //If property value is a named object, use the property name, otherwise it should be a String
            val propertyVal : String = when {
                property.get(event) is Named -> (property.get(event) as Named).name
                else -> property.get(event) as String
            }
            values[property.name] = propertyVal
        }
        return values
    }

    private fun eventValuesMatch(eventValues: Map<String, String>): Boolean {
        eventParams.forEach { entry ->
            val expected = entry.value
            val propertyVal = eventValues[entry.key] ?: ""

            if (expected.toLowerCase() != propertyVal.toLowerCase()) {
                return false
            }
        }
        return true
    }

}