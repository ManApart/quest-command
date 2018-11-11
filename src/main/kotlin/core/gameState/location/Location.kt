package core.gameState.location

import core.utility.Named

class Location(override val name: String, private val description: String = "", val activators: List<String> = listOf(), val creatures: List<String> = listOf(), val items: List<String> = listOf()) : Named {

    override fun toString(): String {
        return name
    }

    fun getDescription() : String {
        return when {
            description.isNotBlank() -> description
            else -> ""
        }
    }

    private fun findOverlap(name: String, args: List<String>): Int {
        var wordCount = 0
        var remainingWords = name.toLowerCase()
        for (i in 0 until args.size) {
            when {
                remainingWords.isBlank() -> return wordCount
                remainingWords.contains(args[i]) -> {
                    remainingWords = remainingWords.substring(remainingWords.indexOf(args[i]))
                    wordCount++
                }
                else -> return wordCount
            }
        }

        return wordCount
    }

}