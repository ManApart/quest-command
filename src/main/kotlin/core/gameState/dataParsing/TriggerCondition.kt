package core.gameState.dataParsing

import core.events.Event
import core.utility.Named
import core.utility.applyParams
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties

class TriggerCondition(private val callingEvent: String, private val eventParams: Map<String, String> = mapOf()) {

    fun applyParamValues(paramValues: Map<String, String>) : TriggerCondition {
        val modifiedParams = applyParams(eventParams, paramValues)
        return TriggerCondition(callingEvent, modifiedParams)
    }

    fun matches(event: Event): Boolean {
        return classMatches(event) && eventValuesMatch(event)
    }

    private fun classMatches(event: Event) = event::class.simpleName == callingEvent

    private fun eventValuesMatch(event: Event) : Boolean {
        eventParams.forEach{ entry ->
            val property = event.javaClass.kotlin.memberProperties.first { it.name == entry.key }

            //Convert param value to proper type
            val expected = when {
                property.returnType.isSubtypeOf(Boolean::class.createType()) -> entry.value.toBoolean()
                property.returnType.isSubtypeOf(Int::class.createType()) -> entry.value.toInt()
                else -> entry.value
            }

            //If property value is a named object, use the proeprty's name, otherwise it should be a String
            val propertyVal = when {
                property.get(event) is Named ->  (property.get(event) as Named).name
                else -> property.get(event)
            }

            if (expected is String && expected.toLowerCase() != (propertyVal as String).toLowerCase()) {
                return false
            }else if (expected != propertyVal){
               return false
            }
        }
        return true
    }

}