package explore.listen

import core.commands.Command
import core.events.EventManager
import core.thing.Thing

class ListenCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Listen", "lst")
    }

    override fun getDescription(): String {
        return "Listen to your surroundings."
    }

    override fun getManual(): String {
        return """
	Listen - Listen to the sound around you.
	"""
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        EventManager.postEvent(ListenEvent(source))
    }

}