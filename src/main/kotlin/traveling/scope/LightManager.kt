package traveling.scope

import core.GameState
import core.target.Target
import traveling.location.location.LIGHT
import traveling.location.location.Location
import kotlin.math.max
import kotlin.math.min


fun getLightLevel(location: Location): Int {
    val light = location.properties.values.getInt(LIGHT) +
            location.weather.properties.values.getInt(LIGHT) +
            getDayBonus(location)
    return max(0, min(10, light))
}

private fun getDayBonus(location: Location): Int {
    val percentDayComplete = GameState.timeManager.getPercentDayComplete()
    return when {
        !location.properties.tags.has("Outside") -> 0
        percentDayComplete < 20 || percentDayComplete > 80 -> -5
        percentDayComplete == 20 || percentDayComplete == 80 -> -3
        percentDayComplete == 30 || percentDayComplete == 70 -> -2
        else -> 0
    }
}

fun getLightLevel(location: Location, target: Target): Int {
    return getLightLevel(location) + target.properties.values.getInt(LIGHT)
}