package conversation

import conversation.dsl.Dialogue
import conversation.dsl.DialogueTree
import conversation.dsl.DialoguesCollection
import core.DependencyInjector
import core.ai.AIManager
import core.startupLog
import core.utility.Backer
import core.utility.lazyM

object ConversationManager {
    private val dialogues = Backer(::loadDialogue)

    private suspend fun loadDialogue(): List<DialogueTree> {
        startupLog("Loading Dialogue.")
        return DependencyInjector.getImplementation(DialoguesCollection::class).values()
    }

    suspend fun reset() {
        dialogues.reset()
    }

    suspend fun getMatchingDialogue(conversation: Conversation): List<Dialogue> {
        return dialogues.get().flatMap { it.getDialogues(conversation) }
    }
}