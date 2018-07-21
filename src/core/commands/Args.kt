package core.commands

class Args(val args: List<String>, delimiters: List<String> = listOf()) {
    val argsString = args.joinToString(" ")
    val argGroups = parseArgGroups(args, delimiters)

    private fun parseArgGroups(args: List<String>, delimiters: List<String>): List<List<String>> {
        if (delimiters.isEmpty()) {
            return listOf()
        }
        val groups = mutableListOf<List<String>>()
        splitGroup(args, delimiters, groups)

        return groups
    }

    private fun splitGroup(args: List<String>, delimiters: List<String>, groups: MutableList<List<String>>) {
        val delimiter = findDelimiter(args, delimiters)
        if (delimiter != -1) {
            groups.add(args.subList(0, delimiter))
            splitGroup(args.subList(delimiter, args.size), delimiters, groups)
        } else {
            groups.add(args)
        }
    }

    private fun findDelimiter(args: List<String>, delimiters: List<String>): Int {
        delimiters.forEach {
            val i = args.indexOf(it)
            if (i != -1) {
                return i
            }
        }
        return -1
    }

}