package conversation.dsl

class DialogueTreesGenerated : DialogueTreesCollection {
    override suspend fun values() = listOf<DialogueTreeResource>(resources.conversation.GenericConversations()).flatMap { it.values() }
}