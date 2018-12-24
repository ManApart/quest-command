package core.commands

class Args(val args: List<String>, private val delimiters: List<String> = listOf(), private val excludedWords: List<String> = listOf()) {
    private val argsString = args.joinToString(" ")
    val argGroups = parseArgGroups()
    val argStrings = argGroups.map { it.joinToString(" ") }

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
     * Returns if the any one of the input words matches one of the words in the arg list
     * An optional condition can be passed in. The condition must be true in order to evaluate hasAny. If the condition is false, hasAny will always return false.
     */
    fun hasAny(words: List<String>, condition: Boolean = true): Boolean {
        return words.any { has(it, condition) }
    }

    /**
     * Returns if the word matches one of the words in the arg list
     * An optional condition can be passed in. The condition must be true in order to evaluate has. If the condition is false, has will always return false.
     */
    fun has(word: String, condition: Boolean = true): Boolean {
        return if (condition) {
            args.any { it.toLowerCase() == word.toLowerCase() }
        } else {
            false
        }
    }

    /**
     * Returns if the word is contained in any of the words in the arg list
     */
    fun contains(word: String): Boolean {
        return args.any { it.toLowerCase().contains(word.toLowerCase()) }
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

    fun argsWithout(words: List<String>): List<String> {
        val lowerCaseWords = words.map { it.toLowerCase() }
        return args.filterNot { lowerCaseWords.contains(it.toLowerCase()) }
    }

    private fun parseArgGroups(): List<List<String>> {
        val groups = mutableListOf<List<String>>()
        if (delimiters.isEmpty()) {
            groups.add(args)
        } else {
            splitGroup(args, groups)
        }

        return groups
    }

    private fun splitGroup(args: List<String>, groups: MutableList<List<String>>) {
        val delimiter = findDelimiter(args)
        if (delimiter != -1) {
            groups.add(removeExcludedWords(args.subList(0, delimiter)))
            splitGroup(args.subList(delimiter + 1, args.size), groups)
        } else {
            groups.add(removeExcludedWords(args))
        }
    }

    private fun removeExcludedWords(list: List<String>): List<String> {
        return list.subtract(excludedWords).toList()
    }

    private fun findDelimiter(args: List<String>): Int {
        delimiters.forEach {
            val i = args.indexOf(it)
            if (i != -1) {
                return i
            }
        }
        return -1
    }

}