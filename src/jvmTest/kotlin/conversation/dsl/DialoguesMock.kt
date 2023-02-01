package conversation.dsl

class DialoguesMock(val values: List<DialogueTree> = listOf()) : DialoguesCollection{
    override suspend fun values() = values
}