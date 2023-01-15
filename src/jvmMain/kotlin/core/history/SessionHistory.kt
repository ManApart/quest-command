package core.history

import core.events.Event
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


actual object SessionHistory {
    private val date = SimpleDateFormat("yyyy-MM-dd-hh-mm").format(Date())
    private const val directory = "./saves/"
    private val fileName = "./saves/session-stats-$date.md"

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

    actual suspend fun saveSessionStats() {
        val directory = File(directory)
        if (!directory.exists()) {
            directory.mkdir()
        }
        File(fileName).printWriter().use { out ->
            out.println(getSessionStats(unknownCommands, eventCounts))
        }
    }
}