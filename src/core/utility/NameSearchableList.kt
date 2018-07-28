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

    fun addProxy(item: N, names: List<String>){
        names.forEach { addProxy(item, it) }
    }

    fun addProxy(item: N, name: String){
        if (!contains(item)){
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
        if (proxies.containsKey(name)){
            return proxies[name]
        }
        return firstOrNull { it.name.toLowerCase().contains(name.toLowerCase()) }
    }

}