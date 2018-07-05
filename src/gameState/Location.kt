package gameState

class Location(val name: String, val description: String = "", val restricted: Boolean = false, val locations: List<Location> = listOf()) {
    private var parent: Location = this

    init {
        locations.forEach { it.setParent(this) }
    }

    override fun toString(): String {
        return name
    }

    private fun setParent(parent: Location) {
        this.parent = parent
    }

    fun findLocation(args: List<String>): Location {
        return if (args.isEmpty()) {
            this
        } else {
            val adjustedArgs = args.flatMap { it.toLowerCase().split(" ") }
            return findChildLocation(adjustedArgs)
        }
    }

    private fun findChildLocation(args: List<String>): Location {
        val child = locations.firstOrNull { location -> location.locationMatches(args) }
        return when {
            child == null -> this
            args.size == 1 -> child
            else -> child.findChildLocation(args.subList(1, args.size))
        }
    }

    private fun locationMatches(args: List<String>): Boolean {
        val nameList = name.toLowerCase().trim().split(" ")
        val argList = args.subList(0, Math.min(args.size, nameList.size))
        return nameList.toTypedArray() contentEquals argList.toTypedArray()
    }

    fun getPath(): List<String> {
        val path = mutableListOf(name)
        if (parent != this) {
            path.addAll(0, parent.getPath())
        }
        return path
    }

    fun getParent(): Location {
        return parent
    }

    fun getRestrictedParent(): Location {
        return if (restricted || parent == this) {
            this
        } else {
            parent.getRestrictedParent()
        }
    }

    fun contains(target: Location): Boolean {
        if (this == target || locations.contains(target)) {
            return true
        } else {
            locations.forEach {
                if (it.contains(target)){
                    return true
                }
            }
        }
        return false
    }
}