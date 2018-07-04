package gameState

class Tags(tags: List<String> = listOf<String>()) {
    private val tags = tags.map { it.toLowerCase() }

    fun has(tag: String) : Boolean{
        return tags.contains(tag.toLowerCase())
    }
}