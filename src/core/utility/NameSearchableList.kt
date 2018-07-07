package core.utility

class NameSearchableList<N : Named> : ArrayList<N>() {

    fun getAll(): List<N> {
        return toList()
    }

    fun exists(name: String) : Boolean{
        return getOrNull(name) != null
    }

    fun exists(name: List<String>) : Boolean{
        if (name.isEmpty()) return false
        val fullName = name.joinToString(" ").toLowerCase()
        return exists(fullName)
    }

    fun get(name: String) : N {
        return getOrNull(name)!!
    }

    fun get(name: List<String>) : N {
        val fullName = name.joinToString(" ").toLowerCase()
        return get(fullName)
    }

    private fun getOrNull(name: String) : N? {
        return firstOrNull { it.name.toLowerCase().contains(name.toLowerCase()) }
    }
}