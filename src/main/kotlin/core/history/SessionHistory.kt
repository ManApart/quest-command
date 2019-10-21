package core.history

import core.events.Event
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object SessionHistory {
    private val date = SimpleDateFormat("yyyy-MM-dd-hh-mm").format(Date())
    private val fileName = "./session-stats-$date.txt"

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
        File(fileName).printWriter().use { out ->
            out.println("---Event Counts--")
            eventCounts.forEach { (eventName, count) ->
                out.println("$eventName: $count")
            }

            out.println("\n---Unknown Commands--")
            unknownCommands.forEach { line ->
                out.println(line)
            }

            out.println("\n---Input History--")
            ChatHistory.history.forEach { inOut ->
                out.println(inOut.input)
            }
        }
    }
}