package system.debug

import core.GameState
import core.events.EventListener
import core.history.display
import core.history.displayYou
import status.stat.StatKind
import traveling.location.weather.WeatherManager

class DebugListListener : EventListener<DebugListEvent>() {
    override fun execute(event: DebugListEvent) {
        event.source.displayYou("Gamestate properties are: " + GameState.properties.toString())
    }
}

class DebugToggleListener : EventListener<DebugToggleEvent>() {
    override fun execute(event: DebugToggleEvent) {
        if (event.debugType == DebugType.DEBUG_GROUP) {
            GameState.putDebug(DebugType.LEVEL_REQ, event.toggledOn)
            GameState.putDebug(DebugType.STAT_CHANGES, event.toggledOn)
            GameState.putDebug(DebugType.RANDOM_SUCCEED, event.toggledOn)
            event.source.displayYou("Gamestate properties are: " + GameState.properties.toString())
        } else {
            GameState.putDebug(event.debugType, event.toggledOn)
            event.source.displayYou("Set ${event.debugType.propertyName} to ${GameState.getDebugBoolean(event.debugType)}")
        }
    }
}

class DebugStatListener : EventListener<DebugStatEvent>() {
    override fun execute(event: DebugStatEvent) {
        if (event.statKind == StatKind.LEVELED) {
            event.target.soul.setStat(event.statName, event.level)
            val newStat = event.target.soul.getStatOrNull(event.statName)
            event.target.displayYou("Set ${event.target}'s ${event.statName} to ${newStat?.current} / ${newStat?.max}")
        } else {
            event.target.properties.values.put(event.statName, event.level)
            event.target.displayYou("Set ${event.target}'s ${event.statName} to ${event.target.properties.values.getInt(event.statName)}")
        }
    }
}

class DebugTagListener : EventListener<DebugTagEvent>() {
    override fun execute(event: DebugTagEvent) {
        if (event.isAddingTag) {
            event.target.properties.tags.add(event.tag)
        } else {
            event.target.properties.tags.remove(event.tag)
        }
        event.target.displayYou("${event.target}'s tags are now ${event.target.properties.tags}")
    }
}

class DebugWeatherListener : EventListener<DebugWeatherEvent>() {
    override fun execute(event: DebugWeatherEvent) {
        if (WeatherManager.weatherExists(event.weather)) {
            val weather = WeatherManager.getWeather(event.weather)
            event.source.currentLocation().updateWeather(weather)
            event.source.displayYou("Updated weather to ${weather.name}.")
        } else {
            event.source.displayYou("Could not find weather ${event.weather}.")
        }

    }
}