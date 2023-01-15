package conversation.parsing

import conversation.dialogue.ParsedDialogue
import core.thing.Thing
import createMockedGame
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SentenceParserTest {

    companion object {
        private val speaker by lazy { Thing("speaker") }
        private val listener by lazy { Thing("listener") }

        @JvmStatic
        @BeforeAll
        fun setup() {
            createMockedGame()

            runBlocking { speaker.location.getLocation().addThing(listener) }
        }
    }

    @Test
    fun basicQuestion() {
        val input = "where listener be?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = ParsedDialogue(speaker, listener, listOf(listener), Verb.BE, null, QuestionType.WHERE)
        runBlocking { assertEquals(expectedEvent, parser.getParsedDialogue()) }
    }

    @Test
    fun verbAlias() {
        val input = "where listener is?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = ParsedDialogue(speaker, listener, listOf(listener), Verb.BE, null, QuestionType.WHERE)
        runBlocking { assertEquals(expectedEvent, parser.getParsedDialogue()) }
    }

    @Test
    fun verbBeforeSubject() {
        val input = "where be listener?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = ParsedDialogue(speaker, listener, listOf(listener), Verb.BE, null, QuestionType.WHERE)
        runBlocking { assertEquals(expectedEvent, parser.getParsedDialogue()) }
    }

    @Test
    fun youReturnsListener() {
        val input = "where be you?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = ParsedDialogue(speaker, listener, listOf(listener), Verb.BE, null, QuestionType.WHERE)
        runBlocking { assertEquals(expectedEvent, parser.getParsedDialogue()) }
    }

    @Test
    fun iReturnsSpeaker() {
        val input = "where be i?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = ParsedDialogue(speaker, listener, listOf(speaker), Verb.BE, null, QuestionType.WHERE)
        runBlocking { assertEquals(expectedEvent, parser.getParsedDialogue()) }
    }

    @Test
    fun impliedYou() {
        val input = "where be?"
        val parser = SentenceParser(speaker, listener, input)
        val expectedEvent = ParsedDialogue(speaker, listener, listOf(listener), Verb.BE, null, QuestionType.WHERE)
        runBlocking { assertEquals(expectedEvent, parser.getParsedDialogue()) }
    }


}
