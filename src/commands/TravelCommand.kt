package commands

import events.TravelStartEvent
import gameState.GameState

class TravelCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Travel", "t")
    }

    override fun getDescription(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun execute(args: List<String>) {
        //TODO  - append player location
        val found = GameState.world.findLocation(args)

        if (found.getPath() == args) {
            println("Posting travel start event")
            EventManager.postEvent(TravelStartEvent())
        } else {
            println("Found ${found.getPath()} instead of ${args.joinToString(" ")}")
        }

    }
}