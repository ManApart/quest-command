package core.history

import core.events.Event

actual object SessionHistory {
    actual fun incEventCount(event: Event) {
        throw NotImplementedError()
    }

    actual fun addUnknownCommand(command: String) {
        throw NotImplementedError()
    }

    actual fun saveSessionStats() {
        throw NotImplementedError()
    }
}