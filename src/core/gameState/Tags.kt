package core.gameState

class Tags(tags: List<String> = listOf<String>()) {
    private val tags = tags.map { it.toLowerCase() }

    fun has(tag: String) : Boolean{
        return tags.contains(tag.toLowerCase())
    }

    fun hasAll(tags: List<String>) : Boolean{
        tags.forEach {
            if (!this.tags.contains(it)){
                return false
            }
        }
        return true
    }

    fun hasNone(tags: List<String>) : Boolean{
        tags.forEach {
            if (this.tags.contains(it)){
                return false
            }
        }
        return true
    }
}