package conversation.input

import conversation.dialogue.ConversationContext

class Evaluation(val result: (ConversationContext) -> String, val conditions: List<(ConversationContext) -> Boolean>) {
}