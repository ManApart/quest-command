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
        val subject = parseSubject(words)

        when {
            questionType == null -> display("Could not parse type of question from '$commandLine'.")
            verb == null -> display("Could not parse verb from '$commandLine'.")
            subject == null -> display("Could not parse subject from '$commandLine'.")
            else -> EventManager.postEvent(QuestionEvent(speaker, listener, questionType, subject, verb))
        }
    }

    private fun parseStatement(words: List<String>, commandLine: String) {
        val verb = parseVerb(words)
        val subject = parseSubject(words)

        when {
            verb == null -> display("Could not parse verb from $commandLine")
            subject == null -> display("Could not parse subject from $commandLine")
            else -> EventManager.postEvent(StatementEvent(speaker, listener, subject, verb))
        }
    }

    private fun parseQuestionType(words: List<String>): QuestionType? {
        if (words.isNotEmpty()) {
            val questionWord = words.first()
            return questionTypeFromWord(questionWord)
        }
        return null
    }

    private fun parseVerb(words: List<String>): Verb? {
        if (words.isNotEmpty()) {
            val questionWord = words.last()
            return verbFromWord(questionWord)
        }
        return null
    }

    private fun parseSubject(words: List<String>): Target? {
        if (words.size > 2) {
            val subjectName = words.subList(1, words.size - 1).joinToString(" ")
            if (subjectName == "you") {
                return listener
            }
            //TODO - eventually will need to find subject in all scopes
            val subjects = speaker.location.getLocation().getTargets(subjectName, speaker)
            return subjects.firstOrNull()
        }
        return null
    }

}