package gameState

class Item(val name: String, val description: String = "", tags: List<String> = listOf(), properties: Map<String, String> = HashMap()) : Target{
    override val tags = Tags(tags)
    val properties = Properties(properties)
    override fun toString(): String {
        return name
    }
}