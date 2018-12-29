package core.events

import core.utility.Named
import kotlin.reflect.full.memberProperties

interface Event {
    fun gameTicks(): Int {
        return 0
    }
}

fun Event.getValues(): Map<String, String>{
    val values = mutableMapOf<String, String>()

    javaClass.kotlin.memberProperties.forEach { property ->
        val propertyVal : String = when {
            property.get(this) is Named -> (property.get(this) as Named).name
            else -> property.get(this).toString()
        }
        values[property.name] = propertyVal
    }
    return values
}