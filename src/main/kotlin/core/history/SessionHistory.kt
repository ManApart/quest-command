package core.history

import core.events.Event
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


object SessionHistory {
    private val date = SimpleDateFormat("yyyy-MM-dd-hh-mm").format(Date())
    private const val directory = "./saves/"
    private val fileName = "./saves/session-stats-$date.md"

    private val unknownCommands = mutableListOf<String>()
    private val eventCounts = mutableListOf<EventCount>()

    fun incEventCount(event: Event) {
        val name = event.javaClass.simpleName
        val last = eventCounts.lastOrNull()
        if (last != null && last.name == name){
            last.count++
        } else {
            eventCounts.add(EventCount(name))
        }
    }

    fun addUnknownCommand(command: String) {
        unknownCommands.add(command)
    }

    fun saveSessionStats() {
        val directory = File(directory)
        if (!directory.exists()) {
            directory.mkdir()
        }
        File(fileName).printWriter().use { out ->

            out.println("\n## Unknown Commands")
            unknownCommands.forEach { line ->
                out.println(line)
            }

            out.println("\n## Input History" +
                    "\n\nTime in seconds | Command" +
                    "\n---|---")
            ChatHistory.history.forEach { inOut ->
                out.println("${inOut.timeTaken / 1000f} | ${inOut.input}")
            }

            out.println("## Event Counts" +
                    "\n\nEvent | Count" +
                    "\n---|---")
            eventCounts.forEach { event ->
                out.println("${event.name} | ${event.count}")
            }
        }
    }
}