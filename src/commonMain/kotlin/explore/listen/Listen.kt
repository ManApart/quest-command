package explore.listen

import core.TagKey.SOUND_DESCRIPTION
import core.TagKey.SOUND_LEVEL
import core.events.EventListener
import core.history.displayToMe
import core.thing.Thing
import core.utility.joinToStringAnd
import status.stat.StatKind
import traveling.location.location.Location
import traveling.location.weather.Weather
import traveling.position.NO_VECTOR
import traveling.position.Vector
import kotlin.math.max

const val SOUND_LEVEL_DEFAULT = 5

data class Sound(val description: String, val level: Int, val distance: Vector, val strength: Int)

class Listen : EventListener<ListenEvent>() {

    override suspend fun complete(event: ListenEvent) {
        val location = event.source.location.getLocation()
        val sounds =
            location.getThingSounds(event.source) + listOfNotNull(location.getSound(), location.weather.getSound())
        val threshold = (sounds.maxOfOrNull { it.strength } ?: 0) - 50

        val filteredSounds = sounds.filter { it.strength > threshold }.sortedBy { it.strength }

        if (filteredSounds.isEmpty()) {
            event.source.displayToMe("It is silent.")
        } else {
            val soundText = filteredSounds.joinToStringAnd {
                if (it.distance == NO_VECTOR) it.description else {
                    "${it.description.replace(".", "")} ${it.distance.direction.directionString()}"
                }
            }
            event.source.displayToMe("You hear $soundText.")
        }
    }

}

private fun Location.getSound(): Sound? {
    if (!properties.values.has(SOUND_DESCRIPTION)) return null
    val description = properties.values.getString(SOUND_DESCRIPTION)
    val level = properties.values.getInt(SOUND_LEVEL, SOUND_LEVEL_DEFAULT)
    val strength = level * 10

    return Sound(description, level, NO_VECTOR, strength)
}

private fun Weather.getSound(): Sound? {
    if (!properties.values.has(SOUND_DESCRIPTION)) return null
    val description = properties.values.getString(SOUND_DESCRIPTION)
    val level = properties.values.getInt(SOUND_LEVEL, SOUND_LEVEL_DEFAULT)
    val strength = level * 10

    return Sound(description, level, NO_VECTOR, strength)
}

private fun Location.getThingSounds(source: Thing): List<Sound> {
    return getThings().mapNotNull { it.getSound(source) }
}

fun Thing.getSound(source: Thing): Sound? {

    val soundEffects = soul.getConditions()
        .flatMap { it.effects }
        .map { it.base }
        .filter { it.statKind == StatKind.PROP_VAL && it.statThing == SOUND_LEVEL}

    val description = soundEffects.firstOrNull()?.description ?: properties.values.getString(SOUND_DESCRIPTION, "")
    if (description.isBlank()) return null

    val level = properties.values.getInt(SOUND_LEVEL, SOUND_LEVEL_DEFAULT)
    val vector = position.minus(source.position)
    val distance = position.getDistance(source.position)
    val strength = max(0, (level * 10) - distance)

    return Sound(description, level, vector, strength)
}
