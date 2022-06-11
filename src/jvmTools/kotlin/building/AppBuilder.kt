@file:JvmName("AppBuilder")
package building

/*
This core.utility 'builds' the app by generated files so they don't have to be generated at runtime.

Currently it runs:
 - Reflection to detect all of the commands and event listeners
 - Discover all resource lists (StoryEventResource), create interface (StoryEventsCollection) and mock files and compile the resources to a single list (StoryEventsGenerated)

*/

fun main() {
    ReflectionTools.generateFiles()
    println("Build complete")
}