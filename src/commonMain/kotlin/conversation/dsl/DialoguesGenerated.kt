package conversation.dsl

class DialoguesGenerated : DialoguesCollection {
    override val values by lazy { listOf<DialogueResource>(resources.conversation.GenericConversations()).flatMap { it.values }}
}