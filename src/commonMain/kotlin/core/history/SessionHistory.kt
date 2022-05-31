package core.history

import core.events.Event

expect object SessionHistory {
    fun incEventCount(event: Event)
    fun addUnknownCommand(command: String)
    fun saveSessionStats()
}