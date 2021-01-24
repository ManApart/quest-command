package quests

import core.events.Event
import core.events.EventListener
class QuestListener : EventListener<Event>() {
    private val listeners = mutableMapOf<Class<*>, MutableList<Quest>>()

    fun getListeners(): Map<Class<*>, List<Quest>> {
        return listeners
    }

    override fun getPriorityRank(): Int {
        return 100
    }

    override fun execute(event: Event) {
        if (listeners.isEmpty()) {
            buildListeners()
        }
        val clazz = event.javaClass
        if (listeners.containsKey(clazz)) {
            val quests = listeners[clazz]?.toList()
            quests?.forEach { quest ->
                val storyEvent = quest.getMatchingEvent(event)
                if (storyEvent != null) {
                    removeListeners(quest)
                    quest.executeEvent(storyEvent, event)
                    if (!quest.complete) {
                        addListeners(quest)
                    }
                }
            }
        }
    }

    override fun reset() {
        listeners.clear()
        buildListeners()
    }

    private fun removeListeners(quest: Quest) {
        quest.getListenedForEvents().forEach { event ->
            val clazz = event.triggeredEvent.triggerEvent
            listeners[clazz]?.remove(quest)
            if (listeners[clazz]?.isEmpty() == true) {
                listeners.remove(clazz)
            }
        }
    }

    private fun buildListeners() {
        QuestManager.quests.forEach {
            addListeners(it)
        }
    }

    private fun addListeners(quest: Quest) {
        quest.getListenedForEvents().forEach { event ->
            val clazz = event.triggeredEvent.triggerEvent
            if (!listeners.containsKey(clazz)) {
                listeners[clazz] = mutableListOf()
            }
            listeners[clazz]?.add(quest)
        }
    }

}