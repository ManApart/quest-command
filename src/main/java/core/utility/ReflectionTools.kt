package core.utility

import core.commands.Command
import core.commands.CommandParser.commands
import core.events.Event
import core.events.EventListener
import org.reflections.Reflections
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties


object ReflectionTools {
    private const val srcPrefix = "src/main/resource"
    private const val commandsFile = "/data/generated/commands.txt"
    private const val eventsFile = "/data/generated/events.txt"
    private const val eventListenersFile = "/data/generated/eventListeners.txt"
    private val reflections = Reflections()

    fun saveAllCommands() {
        saveClassNamesToFile(Command::class, commandsFile)
    }

    fun saveAllEvents() {
        saveClassNamesToFile(Event::class, eventsFile)
    }

    fun saveAllEventListeners() {
        saveClassNamesToFile(EventListener::class, eventListenersFile)
    }

    private fun saveClassNamesToFile(clazz: KClass<*>, file: String) {
        val allClasses = reflections.getSubTypesOf(clazz::class.java)

        File(srcPrefix + file).printWriter().use { out ->
            allClasses.forEach {
                out.println(it.name)
            }
        }
    }

    fun getAllCommands() : List<Class<out Command>> {
        return getClassesFromFile(commandsFile)
    }

    fun getAllEvents() : List<Class<out Event>> {
        return getClassesFromFile(eventsFile)
    }

    fun getEvent(className: String): Class<out Event> {
        return getAllEvents().first { className == it.name.substring(it.name.lastIndexOf(".")+1) }
    }

    fun getAllEventListeners() : List<Class<out EventListener<*>>> {
        return getClassesFromFile(eventListenersFile)
    }

    private fun <E>  getClassesFromFile(file: String) : List<Class<E>> {
        val classes = mutableListOf<Class<E>>()
        val content = this::class.java.getResource(file).readText()
        content.trim().split("\r\n").forEach {
            val kClass = Class.forName(it) as Class<E>
            classes.add(kClass)
        }
        return classes.toList()
    }

    fun <R: Any> getProperty(instance: Any, propertyName: String) : R {
        return instance.javaClass.kotlin.memberProperties.first { it.name == propertyName }.get(instance) as R
    }

}