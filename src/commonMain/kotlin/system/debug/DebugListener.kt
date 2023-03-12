package system.debug

import core.GameState
import core.events.EventListener
import core.history.displayToMe
import status.stat.StatKind
import traveling.location.weather.WeatherManager

class DebugListListener : EventListener<DebugListEvent>() {
    override suspend fun complete(event: DebugListEvent) {
        event.source.displayToMe("Gamestate properties are: " + GameState.properties.toString())
    }
}

class DebugToggleListener : EventListener<DebugToggleEvent>() {
    override suspend fun complete(event: DebugToggleEvent) {
        event.debugTypes.forEach { debugType ->
            GameState.putDebug(debugType, event.toggledOn)
            event.source.displayToMe("Set ${debugType.propertyName} to ${GameState.getDebugBoolean(debugType)}")
        }
    }
}

class DebugStatListener : EventListener<DebugStatEvent>() {
    override suspend fun complete(event: DebugStatEvent) {
        if (event.statKind == StatKind.LEVELED) {
            event.thing.soul.setStat(event.statName, event.level)
            val newStat = event.thing.soul.getStatOrNull(event.statName)
            event.thing.displayToMe("Set ${event.thing.name}'s ${event.statName} to ${newStat?.current} / ${newStat?.max}")
        } else {
            event.thing.properties.values.put(event.statName, event.level)
            event.thing.displayToMe("Set ${event.thing.name}'s ${event.statName} to ${event.thing.properties.values.getInt(event.statName)}")
        }
    }
}

class DebugTagListener : EventListener<DebugTagEvent>() {
    override suspend fun complete(event: DebugTagEvent) {
        if (event.isAddingTag) {
            event.thing.properties.tags.add(event.tag)
        } else {
            event.thing.properties.tags.remove(event.tag)
        }
        event.thing.displayToMe("${event.thing}'s tags are now ${event.thing.properties.tags}")
    }
}

class DebugWeatherListener : EventListener<DebugWeatherEvent>() {
    override suspend fun complete(event: DebugWeatherEvent) {
        if (WeatherManager.weatherExists(event.weather)) {
            val weather = WeatherManager.getWeather(event.weather)
            event.source.currentLocation().updateWeather(weather)
            event.source.displayToMe("Updated weather to ${weather.name}.")
        } else {
            event.source.displayToMe("Could not find weather ${event.weather}.")
        }

    }
}