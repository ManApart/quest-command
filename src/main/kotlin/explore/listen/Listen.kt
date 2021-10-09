package explore.listen

import core.events.EventListener
import core.history.display
import core.target.Target
import core.utility.joinToStringAnd
import traveling.location.location.Location
import traveling.location.weather.Weather
import traveling.position.NO_VECTOR
import traveling.position.Vector
import kotlin.math.max

const val SOUND_DESCRIPTION = "Sound Description"
const val SOUND_LEVEL = "Sound Level"
const val SOUND_LEVEL_DEFAULT = 5

private data class Sound(val description: String, val level: Int, val distance: Vector, val strength: Int)

class Listen : EventListener<ListenEvent>() {

    override fun execute(event: ListenEvent) {
        val location = event.source.location.getLocation()
        val sounds =
            location.getTargetSounds(event.source) + listOfNotNull(location.getSound(), location.weather.getSound())
        val threshold = (sounds.maxOfOrNull { it.strength } ?: 0) - 50

        val filteredSounds = sounds.filter { it.strength > threshold }.sortedBy { it.strength }

        if (filteredSounds.isEmpty()) {
            display("It is silent.")
        } else {
            val soundText = filteredSounds.joinToStringAnd {
                if (it.distance == NO_VECTOR) it.description else {
                    "${it.description} ${it.distance.direction.directionString()}"
                }
            }
            display("You hear $soundText.")
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

private fun Location.getTargetSounds(source: Target): List<Sound> {
    return getTargets().mapNotNull { it.getSound(source) }
}


private fun Target.getSound(source: Target): Sound? {
    if (!properties.values.has(SOUND_DESCRIPTION)) return null

    val description = properties.values.getString(SOUND_DESCRIPTION)
    val level = properties.values.getInt(SOUND_LEVEL, SOUND_LEVEL_DEFAULT)
    val vector = position.minus(source.position)
    val distance = position.getDistance(source.position)
    val strength = max(0, (level * 10) - distance)

    return Sound(description, level, vector, strength)
}