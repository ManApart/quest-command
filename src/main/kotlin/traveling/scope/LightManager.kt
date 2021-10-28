package traveling.scope

import core.GameState
import core.thing.Thing
import core.utility.clamp
import traveling.location.location.Location

const val LIGHT_FALLOFF_RATE = 1
const val MAX_LIGHT = 10
const val LIGHT = "light"
const val LIT_LIGHT = "litLight"

fun Location.getLightLevel(): Int {
    val light = properties.values.getInt(LIGHT) +
            weather.properties.values.getInt(LIGHT) +
            getDayBonus()
    return light.clamp(0, MAX_LIGHT)
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

fun Location.getLightLevel(thing: Thing, lightSources: List<Thing> = this.getLightSources()): Int {
    return (getLightLevel() + thing.properties.values.getInt(LIGHT) + getLightCastOn(thing, lightSources)).clamp(0, MAX_LIGHT)
}

fun Location.getLightSources(): List<Thing> {
    return this.getThingsIncludingInventories().filter { it.properties.values.getInt(LIGHT) != 0 }
    //TODO - use only top level once can hold lantern
//    return this.getThingsIncludingTopLevelInventoriesudingTopLevelInventories().filter { it.properties.values.getInt(LIGHT) != 0 }
}

fun getLightCastOn(thing: Thing, lightSources: List<Thing>): Int {
    return lightSources.sumOf { getLightCastOn(thing, it) }.clamp(0, MAX_LIGHT)
}

fun getLightCastOn(thing: Thing, lightSource: Thing): Int {
    val distance = thing.position.getDistance(lightSource.position)
    val light = lightSource.properties.values.getInt(LIGHT) - (LIGHT_FALLOFF_RATE * distance)
    return light.clamp(0, MAX_LIGHT)
}