package core.events.eventParsers

import conversation.dialogue.DialogueEvent
import conversation.parsing.QuestionType
import conversation.parsing.Verb
import core.target.Target
import injectAllDefaultMocks
import org.junit.BeforeClass
import org.junit.Test
import quests.triggerCondition.TriggeredEvent
import kotlin.test.assertEquals

class DialogueEventParserTest {
    private val parser = DialogueEventParser()

    companion object {
        private val speaker = Target("speaker")
        private val listener = Target("listener")

        @JvmStatic
        @BeforeClass
        fun setupAll() {
            injectAllDefaultMocks()

            speaker.location.getLocation().addTarget(listener)
        }
    }

    @Test
    fun basicDialogueEvent() {
        val initialDialogueEvent = DialogueEvent(speaker, listener, speaker, Verb.BE, null, QuestionType.STATEMENT)
        val expectedEvent = DialogueEvent(listener, speaker, listener, Verb.BE, "here", QuestionType.STATEMENT)

        val triggeredEvent = TriggeredEvent("DialogueEvent", listOf("\$this", "\$speaker", "I", "BE", "statement", "here"))

        val result = parser.parse(triggeredEvent, listener, initialDialogueEvent.getFieldsAsParams())
        assertEquals(expectedEvent, result)
    }

}