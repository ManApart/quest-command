package system

import core.events.Event
import core.events.EventListener
import core.utility.ReflectionTools
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
//        listenerMap[listenerClass] = ArrayList(listenerMap[listenerClass]?.sortedWith(compareBy { it.getPriority() }))
    }

    fun <E : Event> unRegisterListener(listener: EventListener<E>) {
        val listenerClass = getListenedForClass(listener)
        if (listenerMap.containsKey(listenerClass)) {
            listenerMap[listenerClass]?.remove(listener)
        }
    }

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

    fun <E : Event> getNumberOfMatchingListeners(event: E) : Int {
        val listeners = mutableListOf<EventListener<*>>()
        listenerMap[event.javaClass]?.filter { (it as EventListener<E>).shouldExecute(event) }
                ?.forEach {
                    listeners.add(it)
                }
        return listeners.size
    }

    private fun <E : Event> executeEvent(event: E) {
        val listeners = mutableListOf<EventListener<*>>()
        listenerMap[event.javaClass]?.filter { (it as EventListener<E>).shouldExecute(event) }
                ?.forEach {
                    (it as EventListener<E>).event = event
                    listeners.add(it)
                }
        listenerMap[Event::class.java]?.filter { (it as EventListener<Event>).shouldExecute(event) }
                ?.forEach {
                    (it as EventListener<Event>).event = event
                    listeners.add(it)
                }
        listeners.sortBy { it.getPriority() }
        listeners.forEach { it.execute() }
    }

    private fun getListenedForClass(listener: EventListener<*>): Class<*> {
        return (listener.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
    }

}
