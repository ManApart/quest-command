package system.debug

import core.GameState
import core.events.EventListener
import core.history.display
import status.stat.StatKind

class DebugListListener : EventListener<DebugListEvent>() {
    override fun execute(event: DebugListEvent) {
        display( "Gamestate properties are: " + GameState.properties.toString())
    }
}

class DebugToggleListener : EventListener<DebugToggleEvent>() {
    override fun execute(event: DebugToggleEvent) {
        if (event.debugType == DebugType.DEBUG_GROUP) {
            GameState.properties.values.put(DebugType.LEVEL_REQ.propertyName, event.toggledOn)
            GameState.properties.values.put(DebugType.STAT_CHANGES.propertyName, event.toggledOn)
            GameState.properties.values.put(DebugType.RANDOM.propertyName, event.toggledOn)
            display( "Gamestate properties are: " + GameState.properties.toString())
        } else {
            GameState.properties.values.put(event.debugType.propertyName, event.toggledOn)
            display("Set ${event.debugType.propertyName} to ${GameState.properties.values.getBoolean(event.debugType.propertyName)}")
        }
    }
}

class DebugStatListener : EventListener<DebugStatEvent>() {
    override fun execute(event: DebugStatEvent) {
        if (event.statKind == StatKind.LEVELED) {
            event.target.soul.setStat(event.statName, event.level)
            val newStat = event.target.soul.getStatOrNull(event.statName)
            display("Set ${event.target}'s ${event.statName} to ${newStat?.current} / ${newStat?.max}")
        } else {
            event.target.properties.values.put(event.statName, event.level)
            display("Set ${event.target}'s ${event.statName} to ${event.target.properties.values.getInt(event.statName)}")
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
        display("${event.target}'s tags are now ${event.target.properties.tags}")
    }
}