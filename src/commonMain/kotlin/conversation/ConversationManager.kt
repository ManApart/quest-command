package conversation

import conversation.dsl.Dialogue
import conversation.dsl.DialogueTree
import conversation.dsl.DialoguesCollection
import core.DependencyInjector
import core.startupLog
import core.utility.lazyM

object ConversationManager {
    private var dialogues by lazyM { loadDialogue() }

    private fun loadDialogue(): List<DialogueTree> {
        startupLog("Loading Dialogue.")
        return DependencyInjector.getImplementation(DialoguesCollection::class).values
    }

    fun reset() {
        dialogues = loadDialogue()
    }

    suspend fun getMatchingDialogue(conversation: Conversation): List<Dialogue> {
        return dialogues.flatMap { it.getDialogues(conversation) }
    }
}