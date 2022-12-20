package conversation.dsl

class DialoguesGenerated : DialoguesCollection {
    override val values by lazy { listOf<DialogueTreeResource>(resources.conversation.GenericConversations()).flatMap { it.values }}
}