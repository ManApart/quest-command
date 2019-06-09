package core.utility

import core.commands.Command
import core.events.Event
import core.events.EventListener
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import system.DependencyInjector
import java.io.File
import kotlin.reflect.full.memberProperties


object ReflectionTools {
    private const val srcPrefix = "./src/main/resource"
    private const val commandsFile = "/data/generated/commands.txt"
    private const val eventsFile = "/data/generated/events.txt"
    private const val eventListenersFile = "/data/generated/eventListeners.txt"
    private val reflections = Reflections(SubTypesScanner(false))
    private val parser = DependencyInjector.getImplementation(ReflectionParser::class.java)

    val events: List<Class<out Event>> by lazy { parser.events }
    val commands: List<Class<out Command>> by lazy { parser.commands }
    val eventListeners: List<Class<out EventListener<*>>> by lazy { parser.eventListeners }

    fun saveAllCommands() {
        val allClasses = reflections.getSubTypesOf(Command::class.java)
        println("Saving ${allClasses.size} classes for Command")

        File(srcPrefix + commandsFile).printWriter().use { out ->
            allClasses.forEach {
                out.println(it.name)
            }
        }
    }

    fun saveAllEvents() {
        val allClasses = reflections.getSubTypesOf(Event::class.java)
        println("Saving ${allClasses.size} classes for Event")

        File(srcPrefix + eventsFile).printWriter().use { out ->
            allClasses.forEach {
                out.println(it.name)
            }
        }
    }

    fun saveAllEventListeners() {
        val allClasses = reflections.getSubTypesOf(EventListener::class.java)
        println("Saving ${allClasses.size} classes for Event Listener")

        File(srcPrefix + eventListenersFile).printWriter().use { out ->
            allClasses.forEach {
                out.println(it.name)
            }
        }
    }

}