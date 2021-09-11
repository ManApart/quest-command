package core.events

import core.DependencyInjector
import java.lang.reflect.ParameterizedType


object EventManager {
    private val listenerMap = HashMap<Class<*>, ArrayList<EventListener<*>>>()
    private val eventQueue = mutableListOf<Event>()
    private var eventListenersCollection = DependencyInjector.getImplementation(EventListenersCollection::class.java)

    fun registerListeners() {
        eventListenersCollection.values.forEach { registerListener(it) }
    }

    fun reset() {
        listenerMap.values.forEach { list -> list.forEach { listener -> listener.reset() } }
    }

    fun clear() {
        listenerMap.clear()
        eventQueue.clear()
    }

    fun <E : Event> registerListener(listener: EventListener<E>) {
        val listenerClass = getListenedForClass(listener)
        if (!listenerMap.containsKey(listenerClass)) {
            listenerMap[listenerClass] = ArrayList()
        }
        listenerMap[listenerClass]?.add(listener)
//        listenerMap[listenerClass] = ArrayList(listenerMap[listenerClass]?.sortedWith(compareBy { it.getPriorityRank() }))
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

    @Suppress("UNCHECKED_CAST")
    fun <E : Event> getNumberOfMatchingListeners(event: E): Int {
        val listeners = mutableListOf<EventListener<*>>()
        listenerMap[event.javaClass]?.filter { (it as EventListener<E>).shouldExecute(event) }
                ?.forEach {
                    listeners.add(it)
                }
        return listeners.size
    }

    fun getUnexecutedEvents(): List<Event> {
        return eventQueue.toList()
    }

    @Suppress("UNCHECKED_CAST")
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
        listeners.sortBy { it.getPriorityRank() }
        listeners.forEach { it.execute() }
    }

    private fun getListenedForClass(listener: EventListener<*>): Class<*> {
        val parametrizedType = findParametrizedType(listener.javaClass)
        return parametrizedType.actualTypeArguments[0] as Class<*>
    }

    private fun findParametrizedType(clazz: Class<*>): ParameterizedType {
        return if (clazz.genericSuperclass is ParameterizedType) {
            (clazz.genericSuperclass as ParameterizedType)
        } else {
            findParametrizedType(clazz.superclass)
        }
    }

}
