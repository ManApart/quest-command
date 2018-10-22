package core.utility

import core.commands.Command
import core.commands.CommandParser.commands
import core.events.Event
import core.events.EventListener
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import system.EventManager.registerListener
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties


object ReflectionTools {
    private const val srcPrefix = "src/main/resource"
    private const val commandsFile = "/data/generated/commands.txt"
    private const val eventsFile = "/data/generated/events.txt"
    private const val eventListenersFile = "/data/generated/eventListeners.txt"
    private val reflections = Reflections(SubTypesScanner(false))

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

    fun getAllCommands(): List<Class<out Command>> {
        return getClassesFromFile(commandsFile)
    }

    fun getAllEvents(): List<Class<out Event>> {
        return getClassesFromFile(eventsFile)
    }

    fun getEvent(className: String): Class<out Event> {
        return getAllEvents().first { className == it.name.substring(it.name.lastIndexOf(".") + 1) }
    }

    fun getAllEventListeners(): List<Class<out EventListener<*>>> {
        return getClassesFromFile(eventListenersFile)
    }

    private fun <E> getClassesFromFile(file: String): List<Class<E>> {
        val classes = mutableListOf<Class<E>>()
        val content = this::class.java.getResource(file).readText()
        content.trim().split("\r\n").forEach {
            try {
                val kClass = Class.forName(it) as Class<E>
                classes.add(kClass)
            } catch (e: ClassNotFoundException) {
                println("Couldn't find class $it")
                throw e
            }
        }
        return classes.toList()
    }

    fun <R : Any> getProperty(instance: Any, propertyName: String): R {
        return instance.javaClass.kotlin.memberProperties.first { it.name == propertyName }.get(instance) as R
    }

}