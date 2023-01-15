package core.history

import core.events.Event

expect object SessionHistory {
    fun incEventCount(event: Event)
    fun addUnknownCommand(command: String)
    suspend fun saveSessionStats()
}

fun getSessionStats(unknownCommands: Map<String, Int>, eventCounts: List<EventCount>): String {
    var out = "\n## Unknown Commands" +
            "\n\nCommand | Count" +
            "\n---|---"
    unknownCommands.entries.sortedBy { it.key }.forEach { (command, count) ->
        out += "$command | $count"
    }

    out += "\n## Input History" +
            "\n\nTime in seconds | Command" +
            "\n---|---"

    //We could print _all_ histories, but for now just print the main one
    GameLogger.getMainHistory().history.forEach { inOut ->
        out += "${inOut.timeTaken / 1000f} | ${inOut.input}"
    }

    out +=
        "## Event Counts" +
                "\n\nEvent | Count" +
                "\n---|---"

    eventCounts.forEach { event ->
        out += "${event.name} | ${event.count}"
    }
    return out
}