package time

import core.commands.Command
import core.events.EventManager

class ViewTimeCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Time", "tm")
    }

    override fun getDescription(): String {
        return "Time:\n\tView the current time."
    }

    override fun getManual(): String {
        return "\n\tTime - View the current time."
    }

    override fun getCategory(): List<String> {
        return listOf("Debugging")
    }

    override fun execute(keyword: String, args: List<String>) {
        EventManager.postEvent(ViewTimeEvent())
    }

}