package conversation.dialogue

import conversation.Conversation
import conversation.parsing.SentenceParser
import core.Player
import core.events.Event

class DialogueEvent(val speaker: Player, val conversation: Conversation, val line: String) : Event {
    val parsed: ParsedDialogue? by lazy { ((SentenceParser(speaker.thing, conversation.getLatestListener(), line))).parsedDialogue }

    override fun toString(): String {
        return line
    }
}