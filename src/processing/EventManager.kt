package processing

import events.Event
import events.EventListener
import utility.ReflectionTools
import java.lang.reflect.ParameterizedType
import java.util.*


object EventManager {
    private val listenerMap = HashMap<Class<*>, ArrayList<EventListener<*>>>()
    private val eventQueue = mutableListOf<Event>()

    init {
        registerListeners()
    }

    private fun registerListeners() {
        ReflectionTools.getAllEventListeners().map {
            registerListener(it.newInstance())
        }
    }

    fun <E : Event> registerListener(listener: EventListener<E>) {
//        println("Registering $listener")
        val listenerClass = getListenedForClass(listener)
        if (!listenerMap.containsKey(listenerClass)) {
            listenerMap[listenerClass] = ArrayList()
        }
        listenerMap[listenerClass]?.add(listener)
        listenerMap[listenerClass] = ArrayList(listenerMap[listenerClass]?.sortedWith(compareBy { it.getPriority() }))
    }

    fun <E : Event> unRegisterListener(listener: EventListener<E>) {
        val listenerClass = getListenedForClass(listener)
        if (listenerMap.containsKey(listenerClass)) {
            listenerMap[listenerClass]?.remove(listener)
        }
    }
//
//    fun <E : Event> postEvent(event: E) {
//        val eventClass = event.javaClass
//        listenerMap[eventClass]?.forEach { (it as EventListener<E>).handle(event) }
//        listenerMap[Event::class.java]?.forEach { (it as EventListener<Event>).handle(event) }
//    }

    /**
     * Posted events will be executed in a FIFO manner
     */
    fun <E : Event> postEvent(event: E) {
        eventQueue.add(event)
    }

    /**
     * Called by the main method to execute the queue of events
     */
    fun executeEvents() {
        val eventCopy = eventQueue.toList()
        eventQueue.clear()
        eventCopy.forEach { event ->
            executeEvent(event)
        }
        if (eventQueue.isNotEmpty()) {
            executeEvents()
        }
    }

    private fun <E : Event> executeEvent(event: E) {
        val eventClass = event.javaClass
        listenerMap[eventClass]?.forEach { (it as EventListener<E>).handle(event) }
        listenerMap[Event::class.java]?.forEach { (it as EventListener<Event>).handle(event) }
    }

    private fun getListenedForClass(listener: EventListener<*>): Class<*> {
        return (listener.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
    }

}
