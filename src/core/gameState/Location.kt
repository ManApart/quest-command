package core.gameState

import core.utility.NameSearchableList
import core.utility.Named

class Location(override val name: String, private val description: String = "", val restricted: Boolean = false, val position: Position = Position(), val activators: List<String> = listOf(), val items: List<String> = listOf(), val locations: NameSearchableList<Location> = NameSearchableList()) : Named {
    private var parent: Location = this

    init {
        locations.forEach { it.setParent(this) }
    }

    override fun toString(): String {
        return name
    }

    fun getDescription() : String {
        return when {
            description.isNotBlank() -> description
            parent.description.isNotBlank() -> parent.description
            else -> ""
        }
    }

    private fun setParent(parent: Location) {
        this.parent = parent
    }

    fun findChildLocation(direction: Direction): List<Location> {
        return findLocation(direction, locations)
    }

    fun findSiblings(direction: Direction): List<Location> {
        return findLocation(direction, parent.locations)
    }

    private fun findLocation(direction: Direction, locations: List<Location>): List<Location> {
        return locations.filter{
            it != this && position.getDirection(it.position) == direction
        }
    }

    //TODO - test
    fun findLeastDistant(locations: List<Location>) : Location {
        return locations.sortedBy { position.getDistance(it.position) }.first()
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
        return if (child == null) {
            this
        } else {
            val childNameWordCount = findOverlap(child.name, args)
            if (args.size == childNameWordCount) {
                child
            } else {
                child.findChildLocation(args.subList(childNameWordCount, args.size))
            }
        }
    }

    private fun findOverlap(name: String, args: List<String>): Int {
        var wordCount = 0
        var remainingWords = name.toLowerCase()
        for (i in 0 until args.size) {
            when {
                remainingWords.isBlank() -> return wordCount
                remainingWords.contains(args[i]) -> {
                    remainingWords = remainingWords.substring(remainingWords.indexOf(args[i]))
                    wordCount++
                }
                else -> return wordCount
            }
        }

        return wordCount
    }

    private fun locationMatches(args: List<String>): Boolean {
        return name.toLowerCase().split(" ").contains(args[0])
    }

    fun getTotalPosition() : Position {
        return addParentPosition(Position())
    }

    private fun addParentPosition(position: Position) : Position {
        val total = position.add(this.position)
        if (parent != this) {
            return parent.addParentPosition(total)
        }
        return total
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
                if (it.contains(target)) {
                    return true
                }
            }
        }
        return false
    }

    companion object {
        fun findLocation(source: Location, args: List<String>): Location {
            if (source.name.toLowerCase() == args.joinToString(" ").toLowerCase()) return source
            val found = GameState.world.findLocation(args)
            if (found == GameState.world) {
                val playerRelativeArgs = (pathAsString(GameState.player.location) + " " + args.joinToString(" ")).split(" ")
                return GameState.world.findLocation(playerRelativeArgs)
            }
            return found
        }

        private fun pathAsString(location: Location): String {
            val path = location.getPath()
            val trimmed = if (path.size > 1 && path[0].toLowerCase() == "world") path.subList(1, path.size) else path
            return trimmed.joinToString(" ").toLowerCase()
        }
    }

}