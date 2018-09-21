package core.utility

import core.commands.Command
import core.commands.CommandParser.commands
import core.events.Event
import core.events.EventListener
import core.history.ChatHistory.history
import org.reflections.Reflections
import java.io.File
import kotlin.reflect.full.memberProperties


object ReflectionTools {
    private const val srcPrefix = "src/main/resource"
    private const val commandsFile = "/data/generated/commands.txt"
    private const val eventsFile = "/data/generated/events.txt"
    private const val eventListenersFile = "/data/generated/eventListeners.txt"

    fun saveAllCommands() : List<Class<out Command>> {
        val reflections = Reflections()
        val allClasses = reflections.getSubTypesOf(Command::class.java)

        File(srcPrefix + commandsFile).printWriter().use { out ->
            allClasses.forEach {
                out.println(it.name)
            }
        }

        return allClasses.toList()
    }

    fun getAllCommands() : List<Class<out Command>> {
        val commands = mutableListOf<Class<out Command>>()
        val content = this::class.java.getResource(commandsFile).readText()
        content.trim().split("\r\n").forEach {
            val kClass: Class<out Command> = Class.forName(it) as Class<out Command>
            commands.add(kClass)
        }
        return commands.toList()
    }

    fun saveAllEvents() : List<Class<out Event>> {
        val reflections = Reflections()
        val allClasses = reflections.getSubTypesOf(Event::class.java)
        File(srcPrefix + eventsFile).printWriter().use { out ->
            allClasses.forEach {
                out.println(it.name)
            }
        }
        return allClasses.toList()
    }

    fun getEvent(className: String): Class<out Event> {
        return getAllEvents().first { className == it.name.substring(it.name.lastIndexOf(".")+1) }
    }

    fun getAllEvents() : List<Class<out Event>> {
        val events = mutableListOf<Class<out Event>>()
        val content = this::class.java.getResource(eventsFile).readText()
        content.trim().split("\r\n").forEach {
            val kClass: Class<out Event> = Class.forName(it) as Class<out Event>
            events.add(kClass)
        }
        return events.toList()
    }

    fun saveAllEventListeners() : List<Class<out EventListener<*>>> {
        val reflections = Reflections()
        val allClasses = reflections.getSubTypesOf(EventListener::class.java)

        File(srcPrefix + eventListenersFile).printWriter().use { out ->
            allClasses.forEach {
                out.println(it.name)
            }
        }

        return allClasses.toList()
    }

    fun getAllEventListeners() : List<Class<out EventListener<*>>> {
        val listeners = mutableListOf<Class<out EventListener<*>>>()
        val content = this::class.java.getResource(eventListenersFile).readText()
        content.trim().split("\r\n").forEach {
            val kClass: Class<out EventListener<*>> = Class.forName(it) as Class<out EventListener<*>>
            listeners.add(kClass)
        }
        return listeners.toList()
    }

    fun <R: Any> getProperty(instance: Any, propertyName: String) : R {
        return instance.javaClass.kotlin.memberProperties.first { it.name == propertyName }.get(instance) as R
    }

}