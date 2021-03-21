package conversation.input
import conversation.input.Dialogue

class DialoguesGenerated : DialoguesCollection {
    override val values: List<Dialogue> = listOf(resources.conversation.GenericConversations()).flatMap { it.values }
}