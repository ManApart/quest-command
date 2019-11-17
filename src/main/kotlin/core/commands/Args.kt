package core.commands

import core.gameState.Direction
import core.utility.toLowerCase

class Args(origArgs: List<String>, delimiters: List<String> = listOf(), excludedWords: List<String> = listOf(), flags: List<String> = listOf()) {
    val args = origArgs.toLowerCase()
    private val delimiters = delimiters.toLowerCase()
    private val excludedWords = excludedWords.toLowerCase()
    private val flags = flags.toLowerCase()
    private val argsString = args.joinToString(" ")
    private val foundFlags = findFlags()
    private val delimitedGroups = delimiters.map { it to "" }.toMap().toMutableMap()

    val argGroups = parseArgGroups()
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

    private fun parseArgGroups(): List<List<String>> {
        val groups = mutableListOf<List<String>>()
        if (delimiters.isEmpty()) {
            groups.add(removeExcludedWords(args))
        } else {
            splitGroup(args, groups)
        }

        return groups
    }

    private fun splitGroup(args: List<String>, groups: MutableList<List<String>>) {
        val delimiter = indexOfFirstDelimiter(args)
        if (delimiter != -1) {
            val subGroup = removeExcludedWords(args.subList(0, delimiter))
            if (subGroup.isEmpty()) {
                lookForward(args, delimiter, groups)
            } else {
                addWordsToGroup(groups, subGroup, args[delimiter])
                splitGroup(args.subList(delimiter, args.size), groups)
            }
        } else {
            addWordsToGroup(groups, removeExcludedWords(args))
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

    private fun lookForward(args: List<String>, delimiter: Int, groups: MutableList<List<String>>) {
        val nextDelimiter = indexOfFirstDelimiter(args.subList(1, args.size))
        when {
            nextDelimiter != -1 -> {
                val nextSubGroup = removeExcludedWords(args.subList(delimiter + 1, nextDelimiter + 1))
                addWordsToGroup(groups, nextSubGroup, args[delimiter])
                splitGroup(args.subList(nextDelimiter + 1, args.size), groups)
            }
            delimiter == 0 -> addWordsToGroup(groups, removeExcludedWords(args.subList(1, args.size)), args[delimiter])
            else -> addWordsToGroup(groups, removeExcludedWords(args))
        }
    }

    private fun addWordsToGroup(groups: MutableList<List<String>>, words: List<String>, delimiter: String? = null) {
        groups.add(words)
        if (delimiter != null) {
            delimitedGroups[delimiter] = words.joinToString(" ")
        }
    }

    private fun findFlags(): List<String> {
        val flagVariants = flags + flags.map { "-$it" }
        return args.filter { flagVariants.contains(it) }
    }

    fun getDelimited(delimiter: String): String {
        return delimitedGroups[delimiter] ?: ""
    }

}
