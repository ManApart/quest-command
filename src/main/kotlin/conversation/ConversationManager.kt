package conversation

import conversation.dsl.Dialogue
import conversation.dsl.DialoguesCollection
import core.DependencyInjector
import core.startupLog
import core.utility.lazyM

object ConversationManager {
    private var dialogues by lazyM { loadDialogue() }

    private fun loadDialogue(): List<Dialogue> {
        startupLog("Loading Dialogue.")
        return DependencyInjector.getImplementation(DialoguesCollection::class).values
    }

    fun reset() {
        dialogues = loadDialogue()
    }

    fun getMatchingDialogue(conversation: Conversation): List<Dialogue> {
        return dialogues.filter { it.matches(conversation) }
    }
}