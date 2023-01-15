package conversation.dsl

class DialoguesGenerated : DialoguesCollection {
    override suspend fun values() = listOf<DialogueTreeResource>(resources.conversation.GenericConversations()).flatMap { it.values() }
}