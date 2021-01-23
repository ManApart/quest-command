package quests

import core.events.Event
import core.events.EventListener
//TODO - make map take actual class instead of string
class QuestListener : EventListener<Event>() {
    private val listeners = mutableMapOf<String, MutableList<Quest>>()

    fun getListeners(): Map<String, List<Quest>> {
        return listeners
    }

    override fun getPriorityRank(): Int {
        return 100
    }

    override fun execute(triggeringEvent: Event) {
        if (listeners.isEmpty()) {
            buildListeners()
        }
        val clazz = triggeringEvent.javaClass.simpleName
        if (listeners.containsKey(clazz)) {
            val quests = listeners[clazz]?.toList()
            quests?.forEach { quest ->
                val storyEvent = quest.getMatchingEvent(triggeringEvent)
                if (storyEvent != null) {
                    removeListeners(quest)
                    quest.executeEvent(storyEvent, triggeringEvent)
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
            val clazz = event.triggeredEvent.triggerEvent.simpleName
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
            val clazz = event.triggeredEvent.triggerEvent.simpleName
            if (!listeners.containsKey(clazz)) {
                listeners[clazz] = mutableListOf()
            }
            listeners[clazz]?.add(quest)
        }
    }

}