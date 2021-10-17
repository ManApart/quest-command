package system.debug

import core.GameState
import core.events.EventListener
import core.history.displayToMe
import status.stat.StatKind
import traveling.location.weather.WeatherManager

class DebugListListener : EventListener<DebugListEvent>() {
    override fun execute(event: DebugListEvent) {
        event.source.displayToMe("Gamestate properties are: " + GameState.properties.toString())
    }
}

class DebugToggleListener : EventListener<DebugToggleEvent>() {
    override fun execute(event: DebugToggleEvent) {
        if (event.debugType == DebugType.DEBUG_GROUP) {
            GameState.putDebug(DebugType.LEVEL_REQ, event.toggledOn)
            GameState.putDebug(DebugType.STAT_CHANGES, event.toggledOn)
            GameState.putDebug(DebugType.RANDOM_SUCCEED, event.toggledOn)
            event.source.displayToMe("Gamestate properties are: " + GameState.properties.toString())
        } else {
            GameState.putDebug(event.debugType, event.toggledOn)
            event.source.displayToMe("Set ${event.debugType.propertyName} to ${GameState.getDebugBoolean(event.debugType)}")
        }
    }
}

class DebugStatListener : EventListener<DebugStatEvent>() {
    override fun execute(event: DebugStatEvent) {
        if (event.statKind == StatKind.LEVELED) {
            event.thing.soul.setStat(event.statName, event.level)
            val newStat = event.thing.soul.getStatOrNull(event.statName)
            event.thing.displayToMe("Set ${event.thing}'s ${event.statName} to ${newStat?.current} / ${newStat?.max}")
        } else {
            event.thing.properties.values.put(event.statName, event.level)
            event.thing.displayToMe("Set ${event.thing}'s ${event.statName} to ${event.thing.properties.values.getInt(event.statName)}")
        }
    }
}

class DebugTagListener : EventListener<DebugTagEvent>() {
    override fun execute(event: DebugTagEvent) {
        if (event.isAddingTag) {
            event.thing.properties.tags.add(event.tag)
        } else {
            event.thing.properties.tags.remove(event.tag)
        }
        event.thing.displayToMe("${event.thing}'s tags are now ${event.thing.properties.tags}")
    }
}

class DebugWeatherListener : EventListener<DebugWeatherEvent>() {
    override fun execute(event: DebugWeatherEvent) {
        if (WeatherManager.weatherExists(event.weather)) {
            val weather = WeatherManager.getWeather(event.weather)
            event.source.currentLocation().updateWeather(weather)
            event.source.displayToMe("Updated weather to ${weather.name}.")
        } else {
            event.source.displayToMe("Could not find weather ${event.weather}.")
        }

    }
}