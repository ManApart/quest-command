package conversation.dsl

class DialogueTreesGenerated : DialogueTreesCollection {
    override val values by lazy { listOf<DialogueTreeResource>(resources.conversation.GenericConversations()).flatMap { it.values }}
}