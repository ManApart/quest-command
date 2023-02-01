package conversation.dsl

class DialogueTreesMock(val values: List<DialogueTree> = listOf()) : DialogueTreesCollection {
    override suspend fun values() = values
}