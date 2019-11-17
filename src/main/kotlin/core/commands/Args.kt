package core.commands

import core.gameState.Direction
import core.utility.safeSubList
import core.utility.toLowerCase

class Args(origArgs: List<String>, delimiters: List<String> = listOf(), excludedWords: List<String> = listOf(), flags: List<String> = listOf()) {
    val args = origArgs.toLowerCase()
    private val delimiters = delimiters.toLowerCase()
    private val excludedWords = excludedWords.toLowerCase()
    private val flags = flags.toLowerCase()
    private val argsString = args.joinToString(" ")
    private val foundFlags = findFlags()
    val delimitedGroups = parseArgGroups()

    //TODO - replace arg groups and arg strings with get by delimited
    val argGroups = delimitedGroups.values.toList()
    val argStrings = argGroups.map { it.joinToString(" ") }
    val fullString = origArgs.joinToString(" ")

    override fun toString(): String {
        return argsString
    }

    fun isEmpty(): Boolean {
        return args.isEmpty()
    }

    fun getGroup(i: Int): List<String> {
        return if (i < argGroups.size) {
            argGroups[i]
        } else {
            listOf()
        }
    }

    fun getGroupString(i: Int): String {
        return if (i < argStrings.size) {
            argStrings[i]
        } else {
            ""
        }
    }

    /**
     * Returns any of the input words that match one of the words in the arg list
     * An optional condition can be passed in. The condition must be true in order to evaluate hasAny. If the condition is false, hasAny will always return an empty list.
     */
    fun hasAny(words: List<String>, condition: Boolean = true): List<String> {
        return words.filter { has(it, condition) }
    }

    /**
     * Returns if the word matches one of the words in the arg list
     * An optional condition can be passed in. The condition must be true in order to evaluate has. If the condition is false, has will always return false.
     */
    fun has(word: String, condition: Boolean = true): Boolean {
        return if (condition) {
            args.any { it == word.toLowerCase() }
        } else {
            false
        }
    }

    fun has(regex: Regex): List<String> {
        return argStrings.filter {
            regex.matches(it)
        }
    }

    /**
     * Returns if the word is contained in any of the words in the arg list
     */
    fun contains(word: String): Boolean {
        return args.any { it.contains(word.toLowerCase()) }
    }

    /**
     * Get the first argument that is a number and return it if it exists
     */
    fun getNumber(): Int? {
        args.forEach {
            if (it.toIntOrNull() != null) {
                return it.toInt()
            }
        }
        return null
    }

    /**
     * Returns the number of the indexed arg string if it exists and is a number. Otherwise returns 0
     */
    fun getNumber(index: Int): Int {
        if (index >= 0 && index < args.size) {
            return args[index].toIntOrNull() ?: 0
        }
        return 0
    }

    fun argsWithout(words: List<String>): List<String> {
        val lowerCaseWords = words.map { it.toLowerCase() }
        return args.filterNot { lowerCaseWords.contains(it) }
    }

    fun getDirection(): Direction {
        val directions = hasAny(Direction.values().map { it.name })
        return if (directions.isNotEmpty()) {
            Direction.getDirection(directions.first())
        } else {
            Direction.NONE
        }
    }

    fun hasFlag(flag: String): Boolean {
        val flagAlt = if (flag.startsWith("-")) {
            flag.substring(1)
        } else {
            "-$flag"
        }
        return foundFlags.contains(flag.toLowerCase()) || foundFlags.contains(flagAlt.toLowerCase())
    }

    private fun parseArgGroups(): Map<String, List<String>> {
        return if (delimiters.isEmpty()) {
            mapOf("base" to removeExcludedWords(args))
        } else {
            mapOf("base" to getBaseGroup()) + delimiters.map { it to getDelimitedGroup(it) }
        }
    }

    private fun getBaseGroup(): List<String> {
        val firstDelimiter = indexOfFirstDelimiter(args)
        return if (firstDelimiter == -1) {
            removeExcludedWords(args)
        } else {
            removeExcludedWords(args.safeSubList(0, firstDelimiter))
        }
    }

    private fun getDelimitedGroup(delimiter: String): List<String> {
        val index = args.indexOf(delimiter)
        return if (index == -1) {
            listOf()
        } else {
            val contentStartIndex = index + 1
            val indexOfNextDelimiter = indexOfFirstDelimiter(args.safeSubList(contentStartIndex))
            if (indexOfNextDelimiter == -1) {
                args.safeSubList(contentStartIndex)
            } else {
                args.safeSubList(contentStartIndex, contentStartIndex + indexOfNextDelimiter)
            }
        }
    }


    private fun removeExcludedWords(list: List<String>): List<String> {
        return list.subtract(excludedWords).subtract(foundFlags).toList()
    }

    private fun indexOfFirstDelimiter(args: List<String>): Int {
        delimiters.forEach {
            val i = args.indexOf(it)
            if (i != -1) {
                return i
            }
        }
        return -1
    }

    private fun findFlags(): List<String> {
        val flagVariants = flags + flags.map { "-$it" }
        return args.filter { flagVariants.contains(it) }
    }

    fun getDelimited(delimiter: String): String {
        return delimitedGroups[delimiter]?.joinToString(" ") ?: ""
    }

    fun hasGroup(delimiter: String) : Boolean {
        return !delimitedGroups[delimiter].isNullOrEmpty()
    }

}
