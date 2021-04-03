package time

import core.commands.Command
import core.events.EventManager

class ViewTimeCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Time", "tm")
    }

    override fun getDescription(): String {
        return "View the current time."
    }

    override fun getManual(): String {
        return """
	Time - View the current time."""
    }

    override fun getCategory(): List<String> {
        return listOf("Debugging")
    }

    override fun execute(keyword: String, args: List<String>) {
        EventManager.postEvent(ViewTimeEvent())
    }

}