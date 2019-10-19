package core.history

import core.events.Event
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object SessionHistory {
    private val unknownCommands = mutableListOf<String>()
    private val eventCounts = mutableMapOf<String, Int>()

    fun incEventCount(event: Event) {
        val name = event.javaClass.simpleName
        eventCounts.putIfAbsent(name, 0)
        eventCounts[name] = eventCounts[name]!! + 1
    }

    fun addUnknownCommand(command: String) {
        unknownCommands.add(command)
    }

    fun saveSessionStats() {
        val date = SimpleDateFormat("dd-MM-yyyy").format(Date())
        val fileName = "./session-stats-$date.txt"
        File(fileName).printWriter().use { out ->
            eventCounts.forEach { (eventName, count) ->
                out.println("$eventName: $count")
            }
            unknownCommands.forEach { line ->
                out.println(line)
            }
        }
    }
}