package core.gameState.quests

import core.events.Event
import core.events.EventListener
import core.utility.ReflectionTools

class QuestListener : EventListener<Event>() {
    private val listeners = mutableMapOf<Class<*>, MutableList<Quest>>()

    override fun execute(event: Event) {
        if (listeners.isEmpty()) {
            buildListeners()
        }
        if (listeners.containsKey(event.javaClass)) {
            val quests = listeners[event.javaClass]?.toList()
            quests?.forEach { quest ->
                val stage = quest.getMatchingEvent(event)
                if (stage != null) {
                    removeListeners(quest)
                    quest.executeEvent(stage)
                    if (!quest.complete) {
                        addListeners(quest)
                    }
                }
            }
        }
    }

    private fun removeListeners(quest: Quest) {
        quest.getListenedForEvents().forEach { event ->
            listeners[event.javaClass]?.remove(quest)
        }
    }

    private fun buildListeners() {
        QuestManager.quests.forEach {
            addListeners(it)
        }
    }

    private fun addListeners(quest: Quest) {
        quest.getListenedForEvents().forEach { event ->
            val clazz = ReflectionTools.getEvent(event.condition.callingEvent)
            if (!listeners.containsKey(clazz)) {
                listeners[clazz] = mutableListOf()
            }
            listeners[clazz]?.add(quest)
        }
    }

}