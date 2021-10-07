package explore.listen

import core.events.EventListener
import core.history.display
import core.target.Target
import traveling.position.Vector

const val SOUND_DESCRIPTION = "Sound Description"
const val SOUND_LEVEL = "Sound Level"
const val SOUND_LEVEL_DEFAULT = 5

data class Sound(val description: String, val level: Int, val distance: Vector)

class Listen : EventListener<ListenEvent>() {

    override fun execute(event: ListenEvent) {
        val sounds = event.source.location.getLocation().getTargets().mapNotNull { it.getSound(event.source) }

        /*
         * TODO
         *  Filter out sounds too far away
         *  Loud sounds drown out softer sounds
         *  Give each sound a general direction (hear x to your west)
         *  Include sounds from weather
         *  Include sounds from location
         */
        val soundText = sounds.joinToString(", ") { it.description }

        display("You hear $soundText.")
    }

}

private fun Target.getSound(source: Target): Sound? {
    if (!properties.values.has(SOUND_DESCRIPTION)) return null
    val description = properties.values.getString(SOUND_DESCRIPTION)
    val level = properties.values.getInt(SOUND_LEVEL, SOUND_LEVEL_DEFAULT)
    val dist = position.minus(source.position)
    return Sound(description, level, dist)
}