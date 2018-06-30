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
        println("Input args ${args.joinToString(", ")}")
        //TODO  - append player location

        val found = GameState.world.findLocation(args)
        val argPath = "world " + args.joinToString(" ")
        val foundPath = found.getPath().joinToString(" ").toLowerCase()

        if (foundPath == argPath) {
            println("Posting travel start event ${found.name}")
            EventManager.postEvent(TravelStartEvent())
        } else {
            println("Found $foundPath instead of $argPath")
        }

    }
}