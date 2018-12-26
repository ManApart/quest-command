package core.utility

class NameSearchableList<N : Named>() : ArrayList<N>() {
    private val proxies = HashMap<String, N>()

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
        if (!contains(item)) {
            add(item)
        }
        proxies[name] = item
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
        return getOrNull(name)!!
    }

    fun getAll(name: String): List<N> {
        val includingDuplicates = filter { it.name.toLowerCase().contains(name.toLowerCase()) } +
                proxies.filter { it.key.contains(name) }.map { it.value }

        val results = mutableSetOf<N>()
        includingDuplicates.forEach { results.add(it) }
        return results.toList()
    }

    fun getOrNull(name: String): N? {
        if (proxies.containsKey(name)) {
            return proxies[name]
        }
        val matches = getAll(name)
        return when {
            matches.isEmpty() -> null
            matches.size == 1 -> matches[0]
            else -> bestMatch(matches, name)
        }
    }

    private fun bestMatch(matches: List<N>, name: String): N {
        return firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
                ?: matches[0]
    }

}