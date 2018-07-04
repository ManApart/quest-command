package gameState

class Item(val name: String, val description: String = "", val tags: List<String> = listOf()) : Target{
    override fun toString(): String {
        return name
    }
}