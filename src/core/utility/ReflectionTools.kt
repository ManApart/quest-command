package core.utility

import core.commands.Command
import core.events.Event
import core.events.EventListener
import org.reflections.Reflections
import interact.actions.Action
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties


object ReflectionTools {

    fun getAllCommands() : List<Class<out Command>> {
        val reflections = Reflections()
        val allClasses = reflections.getSubTypesOf(Command::class.java)
        return allClasses.toList()
    }

    fun getEvent(className: String): Class<out Event> {
        return getAllEvents().first { className == it.name.substring(it.name.lastIndexOf(".")+1) }
    }

    fun getAllEvents() : List<Class<out Event>> {
        val reflections = Reflections()
        val allClasses = reflections.getSubTypesOf(Event::class.java)
        return allClasses.toList()
    }

    fun getAllUses() : List<Class<out Action>> {
        val reflections = Reflections("interact.actions")
        val allClasses = reflections.getSubTypesOf(Action::class.java)
        return allClasses.toList()
    }

    fun getAllEventListeners() : List<Class<out EventListener<*>>> {
        val reflections = Reflections()
        val allClasses = reflections.getSubTypesOf(EventListener::class.java)
        return allClasses.toList()
    }

    fun <R: Any> getProperty(instance: Any, propertyName: String) : R {
        return instance.javaClass.kotlin.memberProperties.first { it.name == propertyName }.get(instance) as R
    }

}