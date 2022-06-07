package core.history

import core.events.Event

actual object SessionHistory {
    private val unknownCommands = mutableMapOf<String, Int>()
    private val eventCounts = mutableListOf<EventCount>()

    actual fun incEventCount(event: Event) {
        val name = event::class.simpleName!!
        val last = eventCounts.lastOrNull()
        if (last != null && last.name == name) {
            last.count++
        } else {
            eventCounts.add(EventCount(name))
        }
    }

    actual fun addUnknownCommand(command: String) {
        unknownCommands[command] = (unknownCommands[command] ?: 0) + 1
    }

    actual fun saveSessionStats() {
        throw NotImplementedError()
    }
}