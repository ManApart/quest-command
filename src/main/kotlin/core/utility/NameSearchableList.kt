package core.utility

class NameSearchableList<N : Named>() : ArrayList<N>() {
    private val proxies = HashMap<String, N>()

    companion object {
        fun from(items: List<String>): NameSearchableList<NamedString> {
            return NameSearchableList(items.map { NamedString(it) })
        }
    }

    constructor(item: N) : this() {
        add(item)
    }

    constructor(items: List<N>) : this() {
        addAll(items)
    }

    override fun clear() {
        proxies.clear()
        super.clear()
    }

    fun addProxy(item: N, names: List<String>) {
        names.forEach { addProxy(item, it) }
    }

    private fun addProxy(item: N, name: String) {
        if (!exists(item)) {
            add(item)
        }
        proxies[name.toLowerCase()] = item
    }

    fun exists(target: N): Boolean {
        return contains(target)
    }

    fun exists(name: String): Boolean {
        if (name.isBlank()) {
            return false
        }
        return getOrNull(name) != null
    }


    fun get(name: String): N {
        return getOrNull(name) ?: throw RuntimeException("Could not find $name in list ${toString()}")
    }

    //TODO - eventually sort by best match
    fun getAll(name: String): List<N> {
        if (name.isBlank()) {
            return listOf()
        }
        val includingDuplicates = filter { it.name.toLowerCase().contains(name.toLowerCase()) } +
                proxies.filter { it.key.contains(name) }.map { it.value }

        val results = mutableSetOf<N>()
        includingDuplicates.forEach { results.add(it) }
        return results.toList()
    }

    fun getOrNull(name: String): N? {
        if (containsProxy(name)) {
            return proxies[name]
        }
        val matches = getAll(name)
        return when {
            matches.isEmpty() -> null
            matches.size == 1 -> matches[0]
            else -> bestMatch(matches, name)
        }
    }

    /**
     * Attempts to find a named match for each name (either the name alone or combined with its neighbor names)
     */
    fun getAny(names: List<String>): List<N> {
        return getAnyRecursive(names)
    }

    private fun getAnyRecursive(thisTry: List<String>, stripped: List<String> = listOf()) : List<N> {
        if (thisTry.isEmpty()) {
            return listOf()
        }
        val items = mutableListOf<N>()
        items.addAll(getAll(thisTry.joinToString(" ")))
        if (items.isEmpty() && thisTry.size > 1) {
            items.addAll(getAnyRecursive(thisTry.subList(0, thisTry.size-1), stripped + thisTry.subList(thisTry.size-1, thisTry.size)))
        }
        items.addAll(getAnyRecursive(stripped))
        return items
    }


    private fun containsProxy(name: String): Boolean {
        return proxies.containsKey(name.toLowerCase())
    }

    private fun bestMatch(matches: List<N>, name: String): N {
        return firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
                ?: matches[0]
    }

}