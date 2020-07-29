package conversation

import conversation.dialogue.DialogueEvent
import core.events.EventManager
import core.history.display
import core.target.Target
import core.utility.Named
import traveling.location.location.NOWHERE_NODE

class SentenceParser(private val speaker: Target, private val listener: Target, sentenceToParse: String) {
    private val isQuestion = sentenceToParse.endsWith("?")
    private val sentence = sentenceToParse.replace("?", "")
    private val words = sentence.split(" ")

    val event by lazy { parseEvent() }

    private fun parseEvent(): DialogueEvent? {
        return if (isQuestion) {
            parseQuestion()
        } else {
            parseStatement()
        }
    }

    private fun parseQuestion(): DialogueEvent? {
        val questionType = parseQuestionType(words)
        val verb = parseVerb(words)
        val verbOptions = parseVerbOptions(words, verb)
        val ignoredWords = createIgnoredWords(questionType, verb, verbOptions)
        val subject = parseSubject(words, ignoredWords)

        when {
            questionType == null -> display("Could not parse type of question from '$sentence'.")
            verb == null -> display("Could not parse verb from '$sentence'.")
            subject == null -> display("Could not parse subject from '$sentence'.")
            else -> return DialogueEvent(speaker, listener, subject, verb, verbOptions, questionType)
        }
        return null
    }

    private fun parseStatement(): DialogueEvent? {
        val verb = parseVerb(words)
        val verbOptions = parseVerbOptions(words, verb)
        val ignoredWords = createIgnoredWords(null, verb, verbOptions)
        val subject = parseSubject(words, ignoredWords)

        when {
            verb == null -> display("Could not parse verb from $sentence")
            subject == null -> display("Could not parse subject from $sentence")
            else -> return DialogueEvent(speaker, listener, subject, verb, verbOptions)
        }
        return null
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

    private fun parseSubject(words: List<String>, ignoredWords: List<String>): Named? {
        val filteredWords = words.filter { !ignoredWords.contains(it) }

        if (filteredWords.isNotEmpty()) {
            val subjectName = filteredWords.joinToString(" ")
            if (subjectName == "you") {
                return listener
            }
            if (subjectName == "i") {
                return speaker
            }
            return findNamed(subjectName) ?: listener
        }
        return listener
    }

    private fun findNamed(subjectName: String): Named? {
        val subjects = speaker.location.getLocation().getTargets(subjectName, speaker)
        if (subjects.isNotEmpty()) {
            return subjects.first()
        }

        val location = speaker.location.network.findLocation(subjectName)
        if (location != NOWHERE_NODE) {
            return location
        }

        return null
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