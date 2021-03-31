package conversation.dsl

class DialoguesGenerated : DialoguesCollection {
    override val values = listOf<DialogueResource>(resources.conversation.GenericConversations()).flatMap { it.values }
}