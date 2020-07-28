package conversation.dialogue

interface DialogueOptionsParser {
    fun loadConversationDialogue(): List<ConditionalDialogue>
}