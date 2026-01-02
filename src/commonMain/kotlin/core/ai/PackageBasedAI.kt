package core.ai

import conversation.ConversationManager
import conversation.dialogue.DialogueEvent
import core.ai.packages.AIPackage
import core.events.EventManager
import core.events.TemporalEvent
import core.history.display
import core.utility.RandomManager

class PackageBasedAI(val aiPackage: AIPackage) : AI() {
    val previousIdeas = mutableListOf<String>()
    override fun toString(): String {
        return "AI for ${creature.name} using ${aiPackage.name} package"
    }

    override suspend fun takeAction(): Boolean {
        val idea = aiPackage.pickIdea(creature)
        previousIdeas.add(idea.name)
        idea.action(creature).forEach { EventManager.postEvent(it) }
        return idea.takesTurn
    }

    override suspend fun hear(event: DialogueEvent) {
        event.speaker.display(event.line)
        val matches = ConversationManager.getMatchingDialogue(event.conversation)
        val priority = matches.maxOf { it.priority }
        val topMatches = matches.filter { it.priority == priority }
        val response = RandomManager.getRandom(topMatches)
        response.result(event.conversation).forEach { EventManager.postEvent(it) }
    }
}
