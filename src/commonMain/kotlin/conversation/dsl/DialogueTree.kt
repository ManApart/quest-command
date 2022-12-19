package conversation.dsl

import conversation.Conversation

data class DialogueTree(
    private val condition: (Conversation) -> Boolean?,
    private val dialogues: List<Dialogue2> = listOf(),
    private val children: List<DialogueTree> = listOf()
)