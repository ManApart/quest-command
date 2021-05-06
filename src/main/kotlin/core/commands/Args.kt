package core.commands

import core.utility.lowercase
import core.utility.safeSubList
import traveling.direction.Direction

private const val BASE = "base"

class Args(origArgs: List<String>, private val delimiters: List<ArgDelimiter> = listOf(), excludedWords: List<String> = listOf(), flags: List<String> = listOf()) {
    constructor(origArgs: List<String>, delimiters: List<String>) : this(origArgs, delimiters.map { ArgDelimiter(listOf(it)) })

    private val excludedWords = excludedWords.lowercase()
    private val flags = flags.lowercase()
    val args = cleanArgs(origArgs)
    private val foundFlags = findFlags()

    private val groups = parseArgGroups()
    private val argStrings = groups.mapValues { delimiterGroups -> delimiterGroups.value.map { wordGroup -> wordGroup.joinToString(" ") } }
    val fullString = origArgs.joinToString(" ")

    override fun toString(): String {
        return fullString
    }

    private fun cleanArgs(origArgs: List<String>): List<String> {
        return origArgs.extractCommas().filter { it != "" }.addSpaces().lowercase()
    }

    private fun List<String>.extractCommas(): List<String> {
        return if (delimiters.contains(",")) {
            map { extractCommas(it) }.flatten()
        } else {
            this
        }
    }

    private fun List<String>.addSpaces(): List<String> {
        //I don't like that this knows about extract commas
        return if (delimiters.contains(" ") && !contains(",")) {
            map { listOf(it, " ") }.flatten()
        } else {
            this
        }
    }

    private fun extractCommas(word: String): List<String> {
        val i = word.indexOf(",")
        return when {
            word == "," || i == -1 -> listOf(word)
            else -> word.split(",").filter { it != "" && it != "," }.map { listOf(it, ",") }.flatten()
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
            args.any { it == word.lowercase() }
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
        return args.any { it.contains(word.lowercase()) }
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

    fun getBaseNumber(): Int? {
        return getBaseString().toIntOrNull()
    }

    fun getNumber(delimiter: String, includeBaseString: Boolean = false): Int? {
        val strings = if (includeBaseString) {
            getBaseAndStrings(delimiter)
        } else {
            getStrings(delimiter)
        }
        return strings.firstOrNull { it.toIntOrNull() != null }?.toIntOrNull()
    }

    fun getNumbers(delimiter: String, includeBaseString: Boolean = false): List<Int?> {
        val strings = if (includeBaseString) {
            getBaseAndStrings(delimiter)
        } else {
            getStrings(delimiter)
        }
        return strings.map { it.toIntOrNull() }
    }

    fun argsWithout(words: List<String>): List<String> {
        val lowerCaseWords = words.map { it.lowercase() }
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
        return foundFlags.contains(flag.lowercase()) || foundFlags.contains(flagAlt.lowercase())
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
        return list.filter {
            !excludedWords.contains(it) && !foundFlags.contains(it)
        }
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

    /**
     * Get's the first non empty value found based on the order (precedence) that you pass in the delimiters, not the order the phrases are passed in the command
     */
    fun getFirstString(vararg delimiters: String): String {
        delimiters.forEach {
            val result = getStrings(it).firstOrNull()
            if (result != null) {
                return result
            }
        }
        return ""
    }

    private fun getKey(delimiter: String): String {
        return if (delimiter == BASE) {
            BASE
        } else {
            delimiters.firstOrNull { it.contains(delimiter) }?.key ?: ""
        }
    }


}
