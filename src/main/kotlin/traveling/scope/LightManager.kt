package traveling.scope

import core.GameState
import core.target.Target
import traveling.location.location.LIGHT


fun getLightLevel(scope: Scope): Int {
    return scope.location.properties.values.getInt(LIGHT) +
            scope.weather.properties.values.getInt(LIGHT) +
            getDayBonus(scope)
}

private fun getDayBonus(scope: Scope): Int {
    val percentDayComplete = GameState.timeManager.getPercentDayComplete()
    return when {
        !scope.location.properties.tags.has("Outside") -> 0
        percentDayComplete < 20 || percentDayComplete > 80 -> -5
        percentDayComplete == 20 || percentDayComplete == 80 -> -3
        percentDayComplete == 30 || percentDayComplete == 70 -> -2
        else -> 0
    }
}

fun getLightLevel(scope: Scope, target: Target): Int {
    return getLightLevel(scope) + target.properties.values.getInt(LIGHT)
}