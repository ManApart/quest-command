package conversation.parsing

import conversation.dialogue.DialogueEvent
import core.target.Target
import injectAllDefaultMocks
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

class SentenceParserTest {

    companion object {
        private val speaker by lazy { Target("speaker") }
        private val listener by lazy {  Target("listener") }

        @JvmStatic
        @BeforeClass
        fun setup() {
            injectAllDefaultMocks()

            speaker.location.getLocation().addTarget(listener)
        }
    }

    @Test
    fun basicQuestion() {
        val input = "where listener be?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = DialogueEvent(speaker, listener, listener, Verb.BE, null, QuestionType.WHERE)
        assertEquals(expectedEvent, parser.event)
    }

    @Test
    fun verbAlias() {
        val input = "where listener is?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = DialogueEvent(speaker, listener, listener, Verb.BE, null, QuestionType.WHERE)
        assertEquals(expectedEvent, parser.event)
    }

    @Test
    fun verbBeforeSubject() {
        val input = "where be listener?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = DialogueEvent(speaker, listener, listener, Verb.BE, null, QuestionType.WHERE)
        assertEquals(expectedEvent, parser.event)
    }

    @Test
    fun youReturnsListener() {
        val input = "where be you?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = DialogueEvent(speaker, listener, listener, Verb.BE, null, QuestionType.WHERE)
        assertEquals(expectedEvent, parser.event)
    }

    @Test
    fun iReturnsSpeaker() {
        val input = "where be i?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = DialogueEvent(speaker, listener, speaker, Verb.BE, null, QuestionType.WHERE)
        assertEquals(expectedEvent, parser.event)
    }

    @Test
    fun impliedYou() {
        val input = "where be?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = DialogueEvent(speaker, listener, listener, Verb.BE, null, QuestionType.WHERE)
        assertEquals(expectedEvent, parser.event)
    }


}