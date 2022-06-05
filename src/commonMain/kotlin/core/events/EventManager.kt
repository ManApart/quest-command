package core.events

import core.DependencyInjector
import core.utility.getListenedForClass
import kotlin.reflect.KClass

object EventManager {
    private val listenerMap = HashMap<KClass<*>, ArrayList<EventListener<*>>>()
    private val eventQueue = mutableListOf<Event>()
    private var eventListenersCollection = DependencyInjector.getImplementation(EventListenersCollection::class)

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
        return listenerMap[event::class]?.count { (it as EventListener<E>).shouldExecute(event) } ?: 0
    }

    fun getUnexecutedEvents(): List<Event> {
        return eventQueue.toList()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <E : Event> executeEvent(event: E) {
        val specificEvents: List<EventListener<Event>> = (listenerMap[event::class]?.filter { (it as EventListener<E>).shouldExecute(event) }
            ?.map {
                (it as EventListener<E>).event = event
                it
            } ?: emptyList()) as List<EventListener<Event>>
        val genericEventListeners: List<EventListener<Event>> = (listenerMap[Event::class]?.filter { (it as EventListener<Event>).shouldExecute(event) }
            ?.map {
                (it as EventListener<Event>).event = event
                it
            } ?: emptyList())

        (specificEvents + genericEventListeners)
            .sortedBy { it.getPriorityRank() }
            .forEach { it.execute() }
    }

}
