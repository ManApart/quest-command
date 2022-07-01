package core.ai

import conversation.ConversationManager
import conversation.dialogue.DialogueEvent
import core.ai.action.AIAction
import core.events.EventManager
import core.history.display
import core.utility.RandomManager
import use.interaction.nothing.NothingEvent

class ConditionalAI(name: String, private val actions: List<AIAction>) : AI(name) {
    private val defaultAction by lazy { AIAction("Default", mapOf(), listOf(), { _,_ -> listOf(NothingEvent(creature)) }) }

    override fun takeAction() {
        determineAction().execute(creature)
    }

    private fun determineAction(): AIAction {
        val matches = actions.filter { it.canRun(creature) }
        val priority = matches.maxOfOrNull { it.priority } ?: 0
        val topMatches = matches.filter { it.priority == priority }
        return RandomManager.getRandomOrNull(topMatches) ?: defaultAction
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