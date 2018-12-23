package core.utility

import core.commands.Command
import core.events.Event
import core.events.EventListener
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import java.io.File
import kotlin.reflect.full.memberProperties


object ReflectionTools {
    private const val srcPrefix = "./src/main/resource"
    private const val commandsFile = "/data/generated/commands.txt"
    private const val eventsFile = "/data/generated/events.txt"
    private const val eventListenersFile = "/data/generated/eventListeners.txt"
    private val reflections = Reflections(SubTypesScanner(false))

    val events: List<Class<out Event>> by lazy { getAllEvents()}
    val commands: List<Class<out Command>> by lazy { getAllCommands()}
    val eventListeners: List<Class<out EventListener<*>>> by lazy { getAllEventListeners()}

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

    fun getEvent(className: String): Class<out Event> {
        return events.first { className == it.name.substring(it.name.lastIndexOf(".") + 1) }
    }

    private fun getAllCommands(): List<Class<out Command>> {
        return getClassesFromFile(commandsFile)
    }

    private fun getAllEvents(): List<Class<out Event>> {
        return getClassesFromFile(eventsFile)
    }

    private fun getAllEventListeners(): List<Class<out EventListener<*>>> {
        return getClassesFromFile(eventListenersFile)
    }

    private fun <E> getClassesFromFile(file: String): List<Class<E>> {
        val classes = mutableListOf<Class<E>>()
        val content = this::class.java.getResource(file).readText()
        content.trim().lines().forEach {
            try {
                @Suppress("UNCHECKED_CAST")
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
        @Suppress("UNCHECKED_CAST")
        return instance.javaClass.kotlin.memberProperties.first { it.name == propertyName }.get(instance) as R
    }

}