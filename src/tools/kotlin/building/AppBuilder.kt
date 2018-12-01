package building

import core.utility.ReflectionTools

/*
This core.utility 'builds' the app by generated files so they don't have to be generated at runtime.

Currently it runs reflection to detect all of the commands, events, and event listeners

*/

fun main(args: Array<String>) {
    ReflectionTools.saveAllCommands()
    ReflectionTools.saveAllEvents()
    ReflectionTools.saveAllEventListeners()
    println("Build complete")
}