package conversation

import conversation.dsl.Dialogue
import conversation.dsl.DialoguesCollection
import core.DependencyInjector

object ConversationManager {
    private var parser = DependencyInjector.getImplementation(DialoguesCollection::class)
    private var dialogues = parser.values

    fun reset() {
        parser = DependencyInjector.getImplementation(DialoguesCollection::class)
        dialogues = parser.values
    }

    fun getMatchingDialogue(conversation: Conversation): List<Dialogue> {
        return dialogues.filter { it.matches(conversation) }
    }
}