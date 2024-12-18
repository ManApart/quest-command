package core.utility

expect class NameSearchableList<N : Named>() : MutableList<N> {
    constructor(item: N)
    constructor(items: Iterable<N>)
    constructor(items: NameSearchableList<N>)
    constructor(items: Iterable<N>, defaultIfEmpty: N)
    override fun get(index: Int): N
    override fun add(element: N): Boolean
    override fun add(index: Int, element: N)
    override fun addAll(index: Int, elements: Collection<N>): Boolean
    override fun addAll(elements: Collection<N>): Boolean
    override fun removeAll(elements: Collection<N>): Boolean
    override fun retainAll(elements: Collection<N>): Boolean
    override fun set(index: Int, element: N): N
    override fun removeAt(index: Int): N
    override fun listIterator(): MutableListIterator<N>
    override fun listIterator(index: Int): MutableListIterator<N>
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<N>
    override fun isEmpty(): Boolean
    override fun contains(element: N): Boolean
    override fun containsAll(elements: Collection<N>): Boolean
    override fun iterator(): MutableIterator<N>
    override fun indexOf(element: N): Int
    override fun lastIndexOf(element: N): Int
    override val size: Int
    override fun clear()
    override fun toString(): String
    fun <R : Comparable<R>> sortedBy(selector: (N) -> R?): NameSearchableList<N>
    fun filter(predicate: (N) -> Boolean): NameSearchableList<N>
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

inline fun <T: Named> NameSearchableList<T>.filterList(predicate: (T) -> Boolean): NameSearchableList<T> {
    return this.toList().filter(predicate).toNameSearchableList()
}
