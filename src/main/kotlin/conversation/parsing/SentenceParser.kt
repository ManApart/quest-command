package conversation.parsing

import conversation.dialogue.ParsedDialogue
import core.history.display
import core.target.Target
import core.utility.Named
import traveling.location.location.LocationManager

class SentenceParser(private val speaker: Target, private val listener: Target, sentenceToParse: String) {
    private val sentence = Sentence(sentenceToParse.lowercase())

    val parsedDialogue = parseDialogue()

    private fun parseDialogue(): ParsedDialogue? {
        parseQuestionType(sentence)
        parseVerb(sentence)
        parseSubject(sentence)
        parseVerbOptions(sentence)

        when {
            !sentence.hasMapped(PartOfSpeech.QUESTION_TYPE) -> display("Could not parse type of question from '${sentence.sentence}'.")
            !sentence.hasMapped(PartOfSpeech.VERB) -> display("Could not parse verb from '${sentence.sentence}'.")
            else -> return ParsedDialogue(speaker, listener, sentence.subjects, sentence.verb, sentence.verbOptions, sentence.questionType)
        }
        return null
    }

    private fun parseQuestionType(sentence: Sentence) {
        if (sentence.isQuestion) {
            val position = sentence.words.indices.first { questionTypeFromWord(sentence.words[it]) != null }
            sentence.mapWord(position, PartOfSpeech.QUESTION_TYPE)
            sentence.questionType = questionTypeFromWord(sentence.words[position])!!
        }
    }

    private fun parseVerb(sentence: Sentence) {
        val position = sentence.words.indices.first { verbFromWord(sentence.words[it]) != null }
        sentence.mapWord(position, PartOfSpeech.VERB)
        sentence.verb = verbFromWord(sentence.words[position])!!
    }

    private fun parseVerbOptions(sentence: Sentence) {
        if (sentence.hasMapped(PartOfSpeech.VERB)) {
            val i = sentence.words.indexOf(sentence.getWord(PartOfSpeech.VERB)) + 1
            if (i != 0 && i < sentence.words.size) {
                var verbOptions = ""
                (i until sentence.words.size).filter { !sentence.isMapped(it) }.forEach {
                    sentence.mapWord(it, PartOfSpeech.VERB_OPTION)
                    verbOptions += sentence.words[it]
                }

                if (verbOptions.isNotBlank()) {
                    sentence.verbOptions = verbOptions
                }
            }
        }
    }

    private fun parseSubject(sentence: Sentence) {
        val filteredWords = sentence.getUnmappedWords()
        sentence.subjects = findSubject(filteredWords)
        sentence.getUnmappedWordPositions().forEach { sentence.mapWord(it, PartOfSpeech.SUBJECT) }
    }

    private fun findSubject(filteredWords: List<String>): List<Named> {
        val subjectName = filteredWords.joinToString(" ")
        return when {
            subjectName == "you" -> listOf(listener)
            subjectName == "i" -> listOf(speaker)
            subjectName.isNotBlank() -> findNamed(subjectName)
            else -> listOf(listener)
        }
    }

    private fun findNamed(subjectName: String): List<Named> {
        val subjects = speaker.location.getLocation().getTargets(subjectName, speaker)
        val exact = subjects.getExact(subjectName)
        if (exact != null){
            return listOf(exact)
        }
        if (subjects.isNotEmpty()) {
            return subjects
        }

        val locations = LocationManager.findLocationsInAnyNetwork(subjectName)
        val exactMatch = locations.firstOrNull { it.name.lowercase() == subjectName }

        return when {
            exactMatch != null -> listOf(exactMatch)
            locations.isNotEmpty() -> locations
            else -> listOf(listener)
        }
    }
}