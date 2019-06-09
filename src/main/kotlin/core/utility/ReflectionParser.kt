package core.utility

import core.commands.Command
import core.events.Event
import core.events.EventListener

interface ReflectionParser {
    val events: List<Class<out Event>>
    val commands: List<Class<out Command>>
    val eventListeners: List<Class<out EventListener<*>>>
    fun getEvent(className: String): Class<out Event>
}