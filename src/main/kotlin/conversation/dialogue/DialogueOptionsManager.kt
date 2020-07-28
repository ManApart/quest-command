package conversation.dialogue

import core.DependencyInjector

object DialogueOptionsManager {
    private var parser = DependencyInjector.getImplementation(DialogueOptionsJsonParser::class.java)

    private var conversationDialogue = DialogueOptions(parser.loadConversationDialogue())

    fun reset() {
        parser = DependencyInjector.getImplementation(DialogueOptionsJsonParser::class.java)
        conversationDialogue = DialogueOptions(parser.loadConversationDialogue())
    }

    fun getConversationResponses() : DialogueOptions {
        return conversationDialogue
    }
}