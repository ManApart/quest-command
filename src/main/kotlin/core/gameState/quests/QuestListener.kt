package core.gameState.quests

import core.events.Event
import core.events.EventListener

class QuestListener : EventListener<Event>() {
    private val listeners = mutableMapOf<String, MutableList<Quest>>()

    fun getListeners(): Map<String, List<Quest>> {
        return listeners
    }

    override fun getPriorityRank(): Int {
        return 100
    }

    override fun execute(event: Event) {
        if (listeners.isEmpty()) {
            buildListeners()
        }
        val clazz = event.javaClass.simpleName
        if (listeners.containsKey(clazz)) {
            val quests = listeners[clazz]?.toList()
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

    override fun reset() {
        listeners.clear()
        buildListeners()
    }

    private fun removeListeners(quest: Quest) {
        quest.getListenedForEvents().forEach { event ->
            val clazz = event.condition.callingEvent
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
            val clazz = event.condition.callingEvent
            if (!listeners.containsKey(clazz)) {
                listeners[clazz] = mutableListOf()
            }
            listeners[clazz]?.add(quest)
        }
    }

}