package core.ai

import conversation.ConversationManager
import conversation.dialogue.DialogueEvent
import core.events.EventManager
import core.history.display
import core.history.displayGlobal
import core.utility.RandomManager

class ConditionalAI : AI() {
    private val defaultAgenda = Pair("Nothing", 0)
    private var goal: Goal? = null

    override fun takeAction() {
        if (goal == null) {
            goal = determineGoal()
        }
        goal?.step(creature)
        if (goal?.canContinue() == false) {
            goal = null
        }
    }

    private fun determineGoal(): Goal {
        val matches = AIManager.desires.flatMap { it.getDesires(creature) }
        val priority = matches.maxOfOrNull { it.second } ?: 0
        val topMatches = matches.filter { it.second == priority }
        val desire = (RandomManager.getRandomOrNull(topMatches) ?: defaultAgenda).first
        val agenda = AIManager.agendas[desire] ?: AIManager.agendas[defaultAgenda.first]!!.also { displayGlobal("Couldn't find agenda for ${desire}!") }

        return Goal(agenda, priority)
    }

    override fun hear(event: DialogueEvent) {
        event.speaker.display(event.line)
        val matches = ConversationManager.getMatchingDialogue(event.conversation)
        val priority = matches.maxOf { it.priority }
        val topMatches = matches.filter { it.priority == priority }
        val response = RandomManager.getRandom(topMatches)
        response.result(event.conversation).forEach { EventManager.postEvent(it) }
    }

}