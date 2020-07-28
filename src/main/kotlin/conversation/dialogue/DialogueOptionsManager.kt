package conversation.dialogue

import core.DependencyInjector

object DialogueOptionsManager {
    private var parser = DependencyInjector.getImplementation(DialogueOptionsParser::class.java)

    private var conversationDialogue = parser.loadConversationDialogue()

    fun reset() {
        parser = DependencyInjector.getImplementation(DialogueOptionsParser::class.java)
        conversationDialogue = parser.loadConversationDialogue()
    }

    fun getConversationResponses() : List<ConditionalDialogue> {
        return conversationDialogue
    }
}