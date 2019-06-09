package core.utility

import core.commands.Command
import core.events.Event
import core.events.EventListener

class KotlinReflectionParser : ReflectionParser {
    override val events: List<Class<out Event>> by lazy { getAllEvents() }
    override val commands: List<Class<out Command>> by lazy { getAllCommands() }
    override val eventListeners: List<Class<out EventListener<*>>> by lazy { getAllEventListeners() }

    private val commandsFile = "/data/generated/commands.txt"
    private val eventsFile = "/data/generated/events.txt"
    private val eventListenersFile = "/data/generated/eventListeners.txt"

    override fun getEvent(className: String): Class<out Event> {
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
}