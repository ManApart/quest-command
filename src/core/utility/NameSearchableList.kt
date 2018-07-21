package core.utility

class NameSearchableList<N : Named>() : ArrayList<N>() {

    constructor(items: N) : this() {
        add(items)
    }

    constructor(items: List<N>) : this() {
        addAll(items)
    }

    fun exists(name: String): Boolean {
        if (name.isBlank()) {
            return false
        }
        return getOrNull(name) != null
    }

    fun exists(name: List<String>): Boolean {
        if (name.isEmpty()) return false
        val fullName = name.joinToString(" ").toLowerCase()
        return exists(fullName)
    }

    fun get(name: String): N {
        return getOrNull(name)!!
    }

    fun getAll(name: String): List<N> {
        return filter { it.name.toLowerCase().contains(name.toLowerCase()) }
    }

    fun get(name: List<String>): N {
        val fullName = name.joinToString(" ").toLowerCase()
        return get(fullName)
    }

    private fun getOrNull(name: String): N? {
        return firstOrNull { it.name.toLowerCase().contains(name.toLowerCase()) }
    }

}