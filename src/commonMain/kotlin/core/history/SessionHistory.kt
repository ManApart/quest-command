package core.history

import core.events.Event
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


object SessionHistory {
    private val date = SimpleDateFormat("yyyy-MM-dd-hh-mm").format(Date())
    private const val directory = "./saves/"
    private val fileName = "./saves/session-stats-$date.md"

    private val unknownCommands = mutableMapOf<String, Int>()
    private val eventCounts = mutableListOf<EventCount>()

    fun incEventCount(event: Event) {
        val name = event::class.simpleName!!
        val last = eventCounts.lastOrNull()
        if (last != null && last.name == name) {
            last.count++
        } else {
            eventCounts.add(EventCount(name))
        }
    }

    fun addUnknownCommand(command: String) {
        unknownCommands[command] = (unknownCommands[command] ?: 0) + 1
    }

    fun saveSessionStats() {
        val directory = File(directory)
        if (!directory.exists()) {
            directory.mkdir()
        }
        File(fileName).printWriter().use { out ->

            out.println("\n## Unknown Commands"+
                    "\n\nCommand | Count" +
                    "\n---|---")
            unknownCommands.entries.sortedBy { it.key }.forEach { (command, count) ->
                out.println("$command | $count")
            }

            out.println(
                "\n## Input History" +
                        "\n\nTime in seconds | Command" +
                        "\n---|---"
            )
            //We could print _all_ histories, but for now just print the main one
            GameLogger.getMainHistory().history.forEach { inOut ->
                out.println("${inOut.timeTaken / 1000f} | ${inOut.input}")
            }

            out.println(
                "## Event Counts" +
                        "\n\nEvent | Count" +
                        "\n---|---"
            )
            eventCounts.forEach { event ->
                out.println("${event.name} | ${event.count}")
            }
        }
    }
}