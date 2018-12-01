package core.gameState.quests

import core.events.Event
import core.events.EventListener
import core.utility.ReflectionTools

class QuestListener : EventListener<Event>() {
    private val listeners = mutableMapOf<Class<*>, MutableList<Quest>>()

    override fun execute(event: Event) {
        if (listeners.isEmpty()){
            buildListeners()
        }
        if (listeners.containsKey(event.javaClass)) {
            val quests = listeners[event.javaClass]?.toList()
            quests?.forEach { quest ->
                if (quest.matches(event)) {
                    quest.execute()

                    updateQuestListener(event, quest)
                }
            }
        }
    }

    private fun updateQuestListener(event: Event, quest: Quest) {
        listeners[event.javaClass]?.remove(quest)
        if (!quest.complete) {
            listenForQuest(quest)
        }
    }

    private fun buildListeners() {
        QuestManager.quests.forEach {
            listenForQuest(it)
        }
    }

    private fun listenForQuest(quest: Quest){
        val clazz = ReflectionTools.getEvent(quest.activeEvent.condition.callingEvent)
        if (!listeners.containsKey(clazz)){
            listeners[clazz] = mutableListOf()
        }
        listeners[clazz]?.add(quest)
    }

}