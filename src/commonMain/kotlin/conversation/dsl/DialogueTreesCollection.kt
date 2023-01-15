package conversation.dsl
import conversation.dsl.DialogueTree

interface DialogueTreesCollection {
    suspend fun values(): List<DialogueTree>
}