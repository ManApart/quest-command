package gameState

class Item(val name: String, val description: String = "", val tags: List<String> = listOf()){
    override fun toString(): String {
        return name
    }
}