package conversation.dialogue

import conversation.Conversation
import conversation.parsing.SentenceParser
import core.events.Event
import core.thing.Thing

class DialogueEvent(val speaker: Thing, val conversation: Conversation, val line: String) : Event {
    val parsed: ParsedDialogue? by lazy { ((SentenceParser(speaker, conversation.getLatestListener(), line))).parsedDialogue }

    override fun toString(): String {
        return line
    }
}