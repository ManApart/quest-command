package core.commands

import core.gameState.Direction
import core.utility.safeSubList
import core.utility.toLowerCase

private const val BASE = "base"

class Args(origArgs: List<String>, private val delimiters: List<ArgDelimiter> = listOf(), excludedWords: List<String> = listOf(), flags: List<String> = listOf()) {
    constructor(origArgs: List<String>, delimiters: List<String>) : this(origArgs, delimiters.map { ArgDelimiter(listOf(it)) })

    private val excludedWords = excludedWords.toLowerCase()
    private val flags = flags.toLowerCase()
    val args = cleanArgs(origArgs)
    private val foundFlags = findFlags()

    private val groups = parseArgGroups()
    private val argStrings = groups.mapValues { delimiterGroups -> delimiterGroups.value.map { wordGroup -> wordGroup.joinToString(" ") } }
    val fullString = origArgs.joinToString(" ")

    override fun toString(): String {
        return fullString
    }

    private fun cleanArgs(origArgs: List<String>): List<String> {
        return extractCommas(origArgs).toLowerCase()
    }

    private fun extractCommas(args: List<String>): List<String> {
        return if (delimiters.contains(",")) {
            args.map { extractCommas(it) }.flatten()
        } else {
            args
        }
    }

    private fun extractCommas(word: String): List<String> {
        val i = word.indexOf(",")
        return if (i != -1) {
            word.split(",").filter { it != "" }.map { listOf(it, ",") }.flatten()
        } else {
            listOf(word)
        }
    }

    fun isEmpty(): Boolean {
        return args.isEmpty()
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
        return argStrings.values.flatten().filter {
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

    //TODO - replace index with delimiter
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

    //TODO - move to direction parser
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

    private fun parseArgGroups(): Map<String, List<List<String>>> {
        return if (delimiters.isEmpty()) {
            mapOf(BASE to listOf(removeExcludedWords(args)))
        } else {
            val map = mutableMapOf(BASE to listOf(findBaseGroup()))
            delimiters.forEach { map[it.key] = findDelimitedGroup(it) }
            map
        }
    }

    private fun findBaseGroup(): List<String> {
        val firstDelimiter = indexOfFirstDelimiter(args)
        return if (firstDelimiter == -1) {
            removeExcludedWords(args)
        } else {
            removeExcludedWords(args.safeSubList(0, firstDelimiter))
        }
    }

    private fun findDelimitedGroup(delimiter: ArgDelimiter): List<List<String>> {
        val groups = mutableListOf<List<String>>()
        var startIndex = delimiter.indexIn(this.args) + 1
        var newList = findDelimitedGroup(this.args, delimiter)
        while (newList.isNotEmpty()) {
            groups.add(newList)
            startIndex += newList.size
            newList = findDelimitedGroup(args.subList(startIndex, args.size), delimiter)
        }
        return groups
    }

    private fun findDelimitedGroup(args: List<String>, delimiter: ArgDelimiter): List<String> {
        val index = delimiter.indexIn(args)
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
            val i = it.indexIn(args)
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

    fun hasBase(): Boolean {
        return hasGroup(BASE)
    }

    fun hasGroup(delimiter: String): Boolean {
        return !groups[getKey(delimiter)].isNullOrEmpty()
    }

    fun getBaseGroup(): List<String> {
        return getGroup(BASE)
    }

    fun getGroup(delimiter: String): List<String> {
        return getGroups(delimiter).firstOrNull() ?: listOf()
    }

    fun getGroups(delimiter: String): List<List<String>> {
        return groups[getKey(delimiter)] ?: listOf()
    }

    fun getBaseAndGroups(delimiter: String): List<List<String>> {
        return getGroups(BASE) + getGroups(delimiter)
    }

    fun getBaseString(): String {
        return getString(BASE)
    }

    fun getString(delimiter: String): String {
        return getStrings(delimiter).firstOrNull() ?: ""
    }

    fun getStrings(delimiter: String): List<String> {
        return argStrings[getKey(delimiter)] ?: listOf()
    }

    fun getBaseAndStrings(delimiter: String): List<String> {
        return getStrings(BASE) + getStrings(delimiter)
    }

    private fun getKey(delimiter: String): String {
        return if (delimiter == BASE) {
            BASE
        } else {
            delimiters.firstOrNull { it.contains(delimiter) }?.key ?: ""
        }
    }


}
