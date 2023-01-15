package conversation.dsl

interface DialoguesCollection {
    suspend fun values(): List<DialogueTree>
}