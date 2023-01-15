package conversation.dsl

interface DialogueTreeResource {
    suspend fun values(): List<DialogueTree>
}