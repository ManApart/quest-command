package conversation.dialogue

import conversation.Conversation
import conversation.parsing.SentenceParser
import core.events.Event
import core.thing.Thing

class DialogueEvent(val speaker: Thing, val conversation: Conversation, val line: String) : Event {
    suspend fun parsed(): ParsedDialogue? {
        return ((SentenceParser(speaker, conversation.getLatestListener(), line))).getParsedDialogue()
    }
    //TODO
//    val parsed: ParsedDialogue? by lazy { ((SentenceParser(speaker, conversation.getLatestListener(), line))).getParsedDialogue() }

    override fun toString(): String {
        return line
    }
}