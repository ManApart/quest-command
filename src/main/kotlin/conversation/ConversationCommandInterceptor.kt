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
        val verbOptions = parseVerbOptions(words, verb)
        val ignoredWords = createIgnoredWords(questionType, verb, verbOptions)
        val subject = parseSubject(words, ignoredWords)

        when {
            questionType == null -> display("Could not parse type of question from '$commandLine'.")
            verb == null -> display("Could not parse verb from '$commandLine'.")
            subject == null -> display("Could not parse subject from '$commandLine'.")
            else -> EventManager.postEvent(QuestionEvent(speaker, listener, questionType, subject, verb, verbOptions))
        }
    }

    private fun parseStatement(words: List<String>, commandLine: String) {
        val verb = parseVerb(words)
        val verbOptions = parseVerbOptions(words, verb)
        val ignoredWords = createIgnoredWords(null, verb, verbOptions)
        val subject = parseSubject(words, ignoredWords)

        when {
            verb == null -> display("Could not parse verb from $commandLine")
            subject == null -> display("Could not parse subject from $commandLine")
            else -> EventManager.postEvent(StatementEvent(speaker, listener, subject, verb, verbOptions))
        }
    }

    private fun parseQuestionType(words: List<String>): QuestionType? {
        return words.mapNotNull { questionTypeFromWord(it) }.firstOrNull()
    }

    private fun parseVerb(words: List<String>): Verb? {
        return words.mapNotNull { verbFromWord(it) }.firstOrNull()
    }

    private fun parseVerbOptions(words: List<String>, verb: Verb?): String? {
        if (words.isNotEmpty() && verb != null) {
            val i = words.indexOf(verb.name.toLowerCase())
            if (i != -1 && i < words.size) {
                return words.subList(i, words.size).joinToString(" ")
            }
        }
        return null
    }

    private fun parseSubject(words: List<String>, ignoredWords: List<String>): Target? {
        val filteredWords = words.filter { !ignoredWords.contains(it) }

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

    private fun createIgnoredWords(questionType: QuestionType?, verb: Verb?, verbOptions: String?): MutableList<String> {
        val ignoredWords = mutableListOf<String>()
        verbOptions?.split(" ")?.forEach { ignoredWords.add(it) }

        if (questionType != null) {
            ignoredWords.add(questionType.name.toLowerCase())
        }
        if (verb != null) {
            ignoredWords.add(verb.name.toLowerCase())
        }
        return ignoredWords
    }

}