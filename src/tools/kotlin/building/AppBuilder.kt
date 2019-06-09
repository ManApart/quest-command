@file:JvmName("AppBuilder")
package building

import building.json.JsonGenerator
import core.utility.ReflectionTools

/*
This core.utility 'builds' the app by generated files so they don't have to be generated at runtime.

Currently it runs:
 - Reflection to detect all of the commands, events, and event listeners
 - Read all source content json and convert to generated content

*/

fun main() {
    ReflectionTools.saveAllCommands()
    ReflectionTools.saveAllEvents()
    ReflectionTools.saveAllEventListeners()
    JsonGenerator.generate("./src/main/resource", "/data/src/content", "/data/generated/content")
    println("Build complete")
}