package gameState

class Item(val name: String, val description: String = "", tags: List<String> = listOf()) : Target{
    override val tags = Tags(tags)
    override fun toString(): String {
        return name
    }
}