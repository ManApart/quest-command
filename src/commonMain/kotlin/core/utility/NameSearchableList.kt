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

    constructor(items: Iterable<N>) : this() {
        addAll(items)
    }

    constructor(items: Iterable<N>, defaultIfEmpty: N) : this(items) {
        if (isEmpty()) {
            add(defaultIfEmpty)
        }
    }

    constructor(items: NameSearchableList<N>) : this() {
        addAll(items)
        addAllProxies(items)
    }

    override fun clear() {
        proxies.clear()
        super.clear()
    }

    override fun toString(): String {
        return "$size with proxies: $proxies"
    }

    private fun toStringExtended(): String {
        return "[${joinToString { it.name }}] with proxies: $proxies"
    }

    fun <R : Comparable<R>> sortedBy(selector: (N) -> R?): NameSearchableList<N> {
        val newList: NameSearchableList<N> = NameSearchableList(sortedWith(compareBy(selector)))
        newList.addAllProxies(this)
        return newList
    }

    fun filter(predicate: (N) -> Boolean): NameSearchableList<N> {
        val newList: NameSearchableList<N> = filterTo(NameSearchableList(), predicate)
        // Only add proxies for items that still exist in the list
        proxies.forEach { (proxy, item) ->
            if (newList.exists(item)) {
                this.addProxy(item, proxy)
            }
        }
        return newList
    }

    operator fun plus(other: NameSearchableList<N>): NameSearchableList<N> {
        val newList = NameSearchableList(this)
        newList.addAll(other)
        newList.addAllProxies(other)
        return newList
    }

    operator fun plus(other: List<N>): NameSearchableList<N> {
        val newList = NameSearchableList(this)
        newList.addAll(other)
        return newList
    }

    fun addProxy(item: N, names: List<String>) {
        names.forEach { addProxy(item, it) }
    }

    fun addProxy(item: N, name: String) {
        if (!exists(item)) {
            add(item)
        }
        proxies[name.lowercase()] = item
    }

    private fun addAllProxies(other: NameSearchableList<N>) {
        other.proxies.forEach { (proxy, item) -> this.addProxy(item, proxy) }
    }

    fun exists(thing: N): Boolean {
        return contains(thing)
    }

    fun exists(name: String): Boolean {
        if (name.isBlank()) {
            return false
        }
        return getOrNull(name) != null
    }

    fun existsExact(name: String): Boolean {
        if (name.isNotBlank()) {
            val cleaned = name.lowercase().trim()
            return containsProxy(cleaned) || any { cleaned == it.name.lowercase() }
        }
        return false
    }

    fun existsByWholeWord(name: String): Boolean {
        if (name.isNotBlank()) {
            val cleaned = name.lowercase().split(" ")
            return containsProxy(name.lowercase()) || any { item ->
                val itemWords = item.name.lowercase().split(" ")
                cleaned.any { itemWords.contains(it) }
            }
        }
        return false
    }

    fun count(name: String): Int {
        if (name.isBlank()) {
            return 0
        }
        return getAll(name).size
    }

    fun countExact(name: String): Int {
        if (name.isBlank()) {
            return 0
        }
        val proxyCount = if (containsProxy(name.lowercase())) {
            1
        } else {
            0
        }

        val cleaned = name.lowercase().trim()
        return proxyCount + this.filter { item ->
            cleaned == item.name
        }.size
    }

    fun countByWholeWord(name: String): Int {
        if (name.isNotBlank()) {
            val cleaned = name.lowercase().split(" ")
            val proxyCount = if (containsProxy(name.lowercase())) {
                1
            } else {
                0
            }

            return proxyCount + this.filter { item ->
                val itemWords = item.name.lowercase().split(" ")
                cleaned.any { itemWords.contains(it) }
            }.size
        }
        return 0
    }


    fun get(name: String): N {
        return getOrNull(name) ?: throw RuntimeException("Could not find $name in list ${toStringExtended()}")
    }

    //TODO - eventually sort by best match
    fun getAll(name: String): NameSearchableList<N> {
        if (name.isBlank()) {
            return NameSearchableList()
        }
        val includingDuplicates = filter { it.name.lowercase().contains(name.lowercase()) } + proxies.filter { it.key.contains(name) }.map { it.value }

        val results = mutableSetOf<N>()
        includingDuplicates.forEach { results.add(it) }
        return results.toNameSearchableList()
    }

    fun getExact(name: String): N? {
        return firstOrNull { it.name.equals(name, ignoreCase = true) }
    }

    fun getOrNull(name: String): N? {
        if (containsProxy(name)) {
            return proxies[name]
        }
        val matches = getAll(name)
        return when {
            matches.isEmpty() -> getMatchWithoutSpaces(name)
            matches.size == 1 -> matches[0]
            else -> bestMatch(matches, name)
        }
    }

    private fun getMatchWithoutSpaces(name: String): N? {
        val cleaned = name.replace(" ", "")
        return firstOrNull { cleaned == it.name.replace(" ", "") }
    }

    /**
     * Attempts to find a named match for each name (either the name alone or combined with its neighbor names)
     */
    fun getAny(names: List<String>): NameSearchableList<N> {
        return getAnyRecursive(names)
    }

    private fun getAnyRecursive(thisTry: List<String>, stripped: List<String> = listOf()): NameSearchableList<N> {
        if (thisTry.isEmpty()) {
            return NameSearchableList()
        }
        val items = mutableListOf<N>()
        items.addAll(getAll(thisTry.joinToString(" ")))
        if (items.isEmpty() && thisTry.size > 1) {
            items.addAll(getAnyRecursive(thisTry.subList(0, thisTry.size - 1), stripped + thisTry.subList(thisTry.size - 1, thisTry.size)))
        }
        items.addAll(getAnyRecursive(stripped))
        return items.toNameSearchableList()
    }


    private fun containsProxy(name: String): Boolean {
        return proxies.containsKey(name.lowercase())
    }

    private fun bestMatch(matches: List<N>, name: String): N {
        val cleaned = name.lowercase()
        return firstOrNull {
            it.name.lowercase() == cleaned
        } ?: sortedBy { it.name.length }.firstOrNull { named ->
            val cleanedParts = cleaned.split(" ")
            val trialWords = named.name.lowercase().split(" ")
            cleanedParts.any { trialWords.contains(it) }
        } ?: matches[0]
    }

    override fun remove(element: N): Boolean {
        proxies.filterValues { it == element }.keys.forEach { proxies.remove(it) }
        return super.remove(element)
    }

}

fun <T : Named> Iterable<T>.toNameSearchableList(): NameSearchableList<T> {
    return NameSearchableList(this)
}

operator fun <T : Named> List<T>.plus(other: NameSearchableList<T>): NameSearchableList<T> {
    return NameSearchableList(this) + other
}