@file:JvmName("AppBuilder")
package building

import building.json.JsonGenerator
import core.reflection.ReflectionTools

/*
This core.utility 'builds' the app by generated files so they don't have to be generated at runtime.

Currently it runs:
 - Reflection to detect all of the commands and event listeners
 - Read all source content json and convert to generated content
 - Discover all resource lists (StoryEventResource), create interface (StoryEventsCollection) and mock files and compile the resources to a single list (StoryEventsGenerated)

*/

fun main() {
    ReflectionTools.generateFiles()
    JsonGenerator.generate("./src/main/resource", "/data/src/content", "/data/generated/content")
    println("Build complete")
}