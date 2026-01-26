package core.events

import building.ModManager
import core.DependencyInjector
import core.GameState
import core.GameStateKeys.TEST_MODE
import core.ai.directAI
import core.ai.replenishAITurns
import core.history.displayGlobal
import core.thing.Thing
import system.debug.DebugType
import time.gameTick.GameTickEvent

const val MAX_EVENT_LOOPS = 100

object EventManager {
    private var listenerMap = DependencyInjector.getImplementation(EventListenerMapCollection::class).values
    private val eventQueue = mutableListOf<Event>()
    private val eventsInProgress = mutableListOf<TemporalEvent>()

    fun reset() {
        listenerMap = DependencyInjector.getImplementation(EventListenerMapCollection::class).values + ModManager.eventListeners
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
    suspend fun processEvents() {
        var loop = 0
        replenishAITurns()
        while (eventQueue.isNotEmpty() && loop < MAX_EVENT_LOOPS) {
            startEvents()
            loop++
        }
        if (loop == MAX_EVENT_LOOPS) {
            if (GameState.properties.values.getBoolean(TEST_MODE)) throw IllegalStateException("Reached max loops, this should not happen!")
            println("Reached max loops, this should not happen!")
        }
    }

    private suspend fun startEvents() {
        val eventCopy = eventQueue.toList()
        if (GameState.getDebugBoolean(DebugType.VERBOSE_EVENT_QUEUE)) {
            if (eventQueue.isNotEmpty()) {
                displayGlobal("Queue:\n" + eventQueue.joinToString("\n") { "\t $it" })
            }
            if (eventsInProgress.isNotEmpty()) {
                displayGlobal("In Progress:\n" + eventsInProgress.joinToString("\n") { "\t $it" })
            }
        }
        eventQueue.clear()
        //If we're just waiting on the player, allow AI to keep moving
        if (eventCopy.size == 1 && eventCopy.first() is GameTickEvent) replenishAITurns()
        eventCopy.forEach { startEvent(it) }
        val playerTurn = directAI()
        if (!playerTurn) {
            tick()
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <E : Event> getNumberOfMatchingListeners(event: E, ignoring: EventListener<E>): Int {
        return listenerMap[event::class.simpleName!!]?.count { it != ignoring && (it as EventListener<E>).shouldExecute(event) } ?: 0
    }

    fun getUnexecutedEvents(): List<Event> {
        return eventQueue.toList()
    }

    private suspend fun <E : Event> startEvent(event: E) {
        if (event is TemporalEvent) {
            val greatestCurrentTime = event.creature.mind.ai.actions.maxOfOrNull { it.timeLeft } ?: 0
            event.timeLeft += greatestCurrentTime
            if (event.timeLeft > 0) {
                event.creature.mind.ai.actions.add(event)
                eventsInProgress.add(event)
            }
        }

        getListeners(event)
            .forEach { it.start(event) }

        if (event !is TemporalEvent || event.timeLeft == 0) {
            completeEvent(event)
        }
    }

    private suspend fun tick() {
        val timeToPass = eventsInProgress.minOfOrNull { it.timeLeft } ?: 1
        eventsInProgress.forEach { it.timeLeft -= timeToPass }
        completeEvents()
        postEvent(GameTickEvent(timeToPass))
    }

    fun removeInProgressEvents(creature: Thing) {
        creature.mind.ai.actions.clear()
        eventsInProgress.removeAll(eventsInProgress.filter { it.creature == creature })
    }

    private suspend fun completeEvents() {
        val events = eventsInProgress.filter { it.timeLeft <= 0 }
        if (events.isNotEmpty()) {
            eventsInProgress.removeAll(events)
            events.forEach { completeEvent(it) }
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
