package core.events

import core.DependencyInjector

object EventManager {
    private var listenerMap = DependencyInjector.getImplementation(EventListenerMapCollection::class).values
    private val eventQueue = mutableListOf<Event>()

    fun reset() {
        listenerMap = DependencyInjector.getImplementation(EventListenerMapCollection::class).values
        listenerMap.values.forEach { list -> list.forEach { listener -> listener.reset() } }
    }

    fun clear() {
        eventQueue.clear()
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
    suspend fun executeEvents() {
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
    suspend fun <E : Event> getNumberOfMatchingListeners(event: E): Int {
        return listenerMap[event::class.simpleName!!]?.count { (it as EventListener<E>).shouldExecute(event) } ?: 0
    }

    fun getUnexecutedEvents(): List<Event> {
        return eventQueue.toList()
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <E : Event> executeEvent(event: E) {
        val specificEvents: List<EventListener<Event>> = (listenerMap[event::class.simpleName]?.filter { (it as EventListener<E>).shouldExecute(event) }
            ?.map {
                it
            } ?: emptyList()) as List<EventListener<Event>>
        val genericEventListeners: List<EventListener<Event>> = (listenerMap[Event::class.simpleName]?.filter { (it as EventListener<Event>).shouldExecute(event) }
            ?.map {
                (it as EventListener<Event>)
            } ?: emptyList())

        (specificEvents + genericEventListeners)
            .sortedBy { it.getPriorityRank() }
            .forEach { it.execute(event) }
    }

}
