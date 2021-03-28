package conversation.input

class DialoguesGenerated : DialoguesCollection {
    override val values = listOf<DialogueResource>(resources.conversation.GenericConversations()).flatMap { it.values }
}