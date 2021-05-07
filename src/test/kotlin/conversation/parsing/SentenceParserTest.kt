package conversation.parsing

import conversation.dialogue.ParsedDialogue
import core.target.Target
import createMockedGame
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

class SentenceParserTest {

    companion object {
        private val speaker by lazy { Target("speaker") }
        private val listener by lazy { Target("listener") }

        @JvmStatic
        @BeforeClass
        fun setup() {
            createMockedGame()

            speaker.location.getLocation().addTarget(listener)
        }
    }

    @Test
    fun basicQuestion() {
        val input = "where listener be?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = ParsedDialogue(speaker, listener, listOf(listener), Verb.BE, null, QuestionType.WHERE)
        assertEquals(expectedEvent, parser.parsedDialogue)
    }

    @Test
    fun verbAlias() {
        val input = "where listener is?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = ParsedDialogue(speaker, listener, listOf(listener), Verb.BE, null, QuestionType.WHERE)
        assertEquals(expectedEvent, parser.parsedDialogue)
    }

    @Test
    fun verbBeforeSubject() {
        val input = "where be listener?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = ParsedDialogue(speaker, listener, listOf(listener), Verb.BE, null, QuestionType.WHERE)
        assertEquals(expectedEvent, parser.parsedDialogue)
    }

    @Test
    fun youReturnsListener() {
        val input = "where be you?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = ParsedDialogue(speaker, listener, listOf(listener), Verb.BE, null, QuestionType.WHERE)
        assertEquals(expectedEvent, parser.parsedDialogue)
    }

    @Test
    fun iReturnsSpeaker() {
        val input = "where be i?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = ParsedDialogue(speaker, listener, listOf(speaker), Verb.BE, null, QuestionType.WHERE)
        assertEquals(expectedEvent, parser.parsedDialogue)
    }

    @Test
    fun impliedYou() {
        val input = "where be?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = ParsedDialogue(speaker, listener, listOf(listener), Verb.BE, null, QuestionType.WHERE)
        assertEquals(expectedEvent, parser.parsedDialogue)
    }


}
