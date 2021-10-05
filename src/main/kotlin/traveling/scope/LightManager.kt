package traveling.scope

import core.GameState
import core.target.Target
import traveling.location.location.LIGHT
import traveling.location.location.Location
import kotlin.math.max
import kotlin.math.min


fun Location.getLightLevel(): Int {
    val light = properties.values.getInt(LIGHT) +
            weather.properties.values.getInt(LIGHT) +
            getDayBonus()
    return max(0, min(10, light))
}

private fun Location.getDayBonus(): Int {
    val percentDayComplete = GameState.timeManager.getPercentDayComplete()
    return when {
        !properties.tags.has("Outside") -> 0
        percentDayComplete < 20 || percentDayComplete > 80 -> -5
        percentDayComplete == 20 || percentDayComplete == 80 -> -3
        percentDayComplete == 30 || percentDayComplete == 70 -> -2
        else -> 0
    }
}

fun Location.getLightLevel(target: Target): Int {
    return getLightLevel() + target.properties.values.getInt(LIGHT)
}