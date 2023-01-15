package conversation.dsl

import conversation.Conversation

data class DialogueTree(
    private val condition: suspend (Conversation) -> Boolean?,
    private val dialogues: List<Dialogue> = listOf(),
    private val children: List<DialogueTree> = listOf()
) {
    suspend fun getDialogues(conversation: Conversation): List<Dialogue>{
        return if (condition(conversation) == true) {
            dialogues + children.flatMap { it.getDialogues(conversation) }
        } else listOf()
    }
}