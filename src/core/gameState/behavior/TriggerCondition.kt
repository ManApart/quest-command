package core.gameState.behavior

import core.events.Event
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties

class TriggerCondition(private val callingEvent: String, private val eventParams: Map<String, String> = mapOf()) {

    fun applyParamValues(paramValues: Map<String, String>) : TriggerCondition {
        val modifiedParams = mutableMapOf<String, String>()

        eventParams.entries.forEach {
            val key = replaceParams(it.key, paramValues)
            val value = replaceParams(it.value, paramValues)
            modifiedParams[key] = value
        }

        return TriggerCondition(callingEvent, modifiedParams)
    }

    private fun replaceParams(line: String, paramValues: Map<String, String>): String {
        var modified = line
        paramValues.forEach {
            modified = modified.replace("$${it.key}", it.value)
        }
        return modified
    }

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

            if (expected is String && expected.toLowerCase() != (property.get(event) as String).toLowerCase()) {
                return false
            }else if (expected != property.get(event)){
               return false
            }
        }
        return true
    }

}