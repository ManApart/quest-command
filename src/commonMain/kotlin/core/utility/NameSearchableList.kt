package core.utility

expect class NameSearchableList<N : Named>() : MutableList<N> {
    constructor(item: N)
    constructor(items: Iterable<N>)
    constructor(items: NameSearchableList<N>)
    constructor(items: Iterable<N>, defaultIfEmpty: N)
    override fun clear()
    override fun toString(): String
    fun <R : Comparable<R>> sortedBy(selector: (N) -> R?): NameSearchableList<N>
    fun filter(predicate: suspend (N) -> Boolean): NameSearchableList<N>
    operator fun plus(other: NameSearchableList<N>): NameSearchableList<N>
    operator fun plus(other: List<N>): NameSearchableList<N>
    fun addProxy(item: N, names: List<String>)
    fun addProxy(item: N, name: String)
    fun exists(thing: N): Boolean
    fun exists(name: String): Boolean
    fun existsExact(name: String): Boolean
    fun existsByWholeWord(name: String): Boolean
    fun count(name: String): Int
    fun countExact(name: String): Int
    fun countByWholeWord(name: String): Int
    fun get(name: String): N
    fun getAll(name: String): NameSearchableList<N>
    fun getExact(name: String): N?
    fun getOrNull(name: String): N?
    fun getAny(names: List<String>): NameSearchableList<N>
    override fun remove(element: N): Boolean
}

fun <T : Named> Iterable<T>.toNameSearchableList(): NameSearchableList<T> {
    return NameSearchableList(this)
}

fun Iterable<String>.toNameSearchableListOfStrings(): NameSearchableList<NamedString> {
    return NameSearchableList(map { NamedString(it) })
}

operator fun <T : Named> List<T>.plus(other: NameSearchableList<T>): NameSearchableList<T> {
    return NameSearchableList(this) + other
}