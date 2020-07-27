package conversation

import conversation.end.EndConversationEvent
import conversation.question.QuestionEvent
import conversation.statement.StatementEvent
import core.commands.CommandInterceptor
import core.commands.CommandParser
import core.events.EventManager
import core.history.display
import core.target.Target

class ConversationCommandInterceptor(private val speaker: Target, private val listener: Target) : CommandInterceptor {
    override fun parseCommand(line: String) {
        val words = CommandParser.cleanLine(line)
        val commandLine = words.joinToString(" ")

        when {
            (commandLine == "goodbye" || commandLine == "exit") -> EventManager.postEvent(EndConversationEvent())
            commandLine.endsWith("?") -> parseQuestion(words.map { it.replace("?", "") }, commandLine)
            else -> parseStatement(words, commandLine)
        }
        EventManager.executeEvents()
    }

    private fun parseQuestion(words: List<String>, commandLine: String) {
        val questionType = parseQuestionType(words)
        val verb = parseVerb(words)
        val subject = parseSubject(words, verb, questionType)

        when {
            questionType == null -> display("Could not parse type of question from '$commandLine'.")
            verb == null -> display("Could not parse verb from '$commandLine'.")
            subject == null -> display("Could not parse subject from '$commandLine'.")
            else -> EventManager.postEvent(QuestionEvent(speaker, listener, questionType, subject, verb))
        }
    }

    private fun parseStatement(words: List<String>, commandLine: String) {
        val verb = parseVerb(words)
        val subject = parseSubject(words, verb)

        when {
            verb == null -> display("Could not parse verb from $commandLine")
            subject == null -> display("Could not parse subject from $commandLine")
            else -> EventManager.postEvent(StatementEvent(speaker, listener, subject, verb))
        }
    }

    private fun parseQuestionType(words: List<String>): QuestionType? {
        if (words.isNotEmpty()) {
            return words.mapNotNull { questionTypeFromWord(it) }.firstOrNull()
        }
        return null
    }

    private fun parseVerb(words: List<String>): Verb? {
        if (words.isNotEmpty()) {
            return words.mapNotNull { verbFromWord(it) }.firstOrNull()
        }
        return null
    }

    private fun parseSubject(words: List<String>, verb: Verb? = null, questionType: QuestionType? = null): Target? {
        val filteredWords = words.toMutableList()
        if (verb != null) {
            filteredWords.remove(verb.name.toLowerCase())
        }
        if (questionType != null) {
            filteredWords.remove(questionType.name.toLowerCase())
        }

        if (filteredWords.isNotEmpty()) {
            val subjectName = filteredWords.joinToString(" ")
            if (subjectName == "you") {
                return listener
            }
            if (subjectName == "i") {
                return speaker
            }
            //TODO - eventually will need to find subject in all scopes
            val subjects = speaker.location.getLocation().getTargets(subjectName, speaker)
            return subjects.firstOrNull() ?: listener
        }
        return listener
    }

}