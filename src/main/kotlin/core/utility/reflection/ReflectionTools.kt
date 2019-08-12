package core.utility.reflection

import core.commands.Command
import core.events.Event
import core.events.EventListener
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import system.DependencyInjector
import java.io.File


object ReflectionTools {
    private const val srcPrefix = "./src/main/resource"
    private const val commandsFile = "/../kotlin/core/utility/reflection/Commands.kt"
    private const val eventListenersFile = "/../kotlin/core/utility/reflection/EventListeners.kt"
    private val reflections = Reflections(SubTypesScanner(false))

    fun saveAllCommands() {
        val allClasses = reflections.getSubTypesOf(Command::class.java)
        println("Saving ${allClasses.size} classes for Command")

        val top = """
            package core.utility.reflection
            val commands: List<core.commands.Command> = listOf(
                """.trimIndent()

        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }

        File(srcPrefix + commandsFile).printWriter().use { out ->
            out.println(top)
            out.println(classes)
            out.println(")")
        }
    }

    fun saveAllEventListeners() {
        val allClasses = reflections.getSubTypesOf(EventListener::class.java)
        println("Saving ${allClasses.size} classes for Event Listener")

        val top = """
            package core.utility.reflection
            val eventListeners: List<core.events.EventListener<*>> = listOf(
                """.trimIndent()

        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }

        File(srcPrefix + eventListenersFile).printWriter().use { out ->
            out.println(top)
            out.println(classes)
            out.println(")")
        }
    }

}