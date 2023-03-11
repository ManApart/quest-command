package core.events

import core.DependencyInjector

object EventManager {
    private var listenerMap = DependencyInjector.getImplementation(EventListenerMapCollection::class).values
    private val eventQueue = mutableListOf<Event>()
    private val eventsInProgress = mutableListOf<TemporalEvent>()

    fun reset() {
        listenerMap = DependencyInjector.getImplementation(EventListenerMapCollection::class).values
        listenerMap.values.forEach { list -> list.forEach { listener -> listener.reset() } }
    }

    fun clear() {
        eventQueue.clear()
        eventsInProgress.clear()
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
    suspend fun startEvents() {
        val eventCopy = eventQueue.toList()
        eventQueue.clear()
        eventCopy.forEach { startEvent(it) }
        if (eventQueue.isNotEmpty()) {
            startEvents()
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <E : Event> getNumberOfMatchingListeners(event: E): Int {
        return listenerMap[event::class.simpleName!!]?.count { (it as EventListener<E>).shouldExecute(event) } ?: 0
    }

    fun getUnexecutedEvents(): List<Event> {
        return eventQueue.toList()
    }

    private suspend fun <E : Event> startEvent(event: E) {
        if (event is TemporalEvent && event.timeLeft > 0) {
            event.creature.mind.ai.actions.add(event)
            eventsInProgress.add(event)
        }

        getListeners(event)
            .forEach { it.start(event) }

        if (event !is TemporalEvent || event.timeLeft == 0){
            completeEvent(event)
        }

    }

    suspend fun tick() {
        eventsInProgress.forEach { it.timeLeft-- }
        completeEvents()
    }

    private suspend fun completeEvents() {
        val events = eventsInProgress.filter { it.timeLeft <= 0 }
        if (events.isNotEmpty()) {
            eventsInProgress.removeAll(events)
            events.forEach { completeEvent(it) }
            if (eventQueue.isNotEmpty()) {
                startEvents()
            }
        }
    }

    private suspend fun <E : Event> completeEvent(event: E) {
        if (event is TemporalEvent) event.creature.mind.ai.actions.remove(event)
        getListeners(event)
            .forEach { it.complete(event) }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <E : Event> getListeners(event: E): List<EventListener<Event>> {
        val specificEvents: List<EventListener<Event>> = (listenerMap[event::class.simpleName]?.filter { (it as EventListener<E>).shouldExecute(event) }
            ?.map {
                it
            } ?: emptyList()) as List<EventListener<Event>>
        val genericEventListeners: List<EventListener<Event>> = (listenerMap[Event::class.simpleName]?.filter { (it as EventListener<Event>).shouldExecute(event) }
            ?.map {
                (it as EventListener<Event>)
            } ?: emptyList())

        return (specificEvents + genericEventListeners)
            .sortedBy { it.getPriorityRank() }
    }

}
