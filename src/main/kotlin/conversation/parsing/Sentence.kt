package conversation.parsing

import core.utility.Named

class Sentence(sentenceIn: String) {
    val isQuestion = sentenceIn.endsWith("?")
    val sentence = sentenceIn.replace("?", "")
    val words = sentence.split(" ")
    var subjects: List<Named> = listOf()
//    var subject: Named = NOWHERE_NODE
    var questionType = QuestionType.STATEMENT
    var verb = Verb.BE
    var verbOptions: String? = null

    private val sentenceMap = words.indices.associateWith { PartOfSpeech.UNMAPPED }.toMutableMap()

    fun mapWord(position: Int, type: PartOfSpeech) {
        sentenceMap[position] = type
    }

    fun hasMapped(type: PartOfSpeech): Boolean {
        return sentenceMap.values.contains(type)
    }

    fun isMapped(position: Int): Boolean {
        return sentenceMap[position] != PartOfSpeech.UNMAPPED
    }

    fun getWord(type: PartOfSpeech): String {
        return getWords(type).first()
    }

    fun getWords(type: PartOfSpeech): List<String> {
        return sentenceMap.entries.filter { it.value == type }.map { words[it.key] }
    }

    fun getUnmappedWords(): List<String> {
        return getWords(PartOfSpeech.UNMAPPED)
    }

    fun getUnmappedWordPositions(): List<Int> {
        return sentenceMap.entries.filter { it.value == PartOfSpeech.UNMAPPED }.map { it.key }
    }

}