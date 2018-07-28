package core.gameState

class Tags(tags: List<String> = listOf<String>()) {
    private val tags = tags.map { it.toLowerCase() }.toMutableList()

    fun has(tag: String) : Boolean{
        return tags.contains(tag.toLowerCase())
    }

    fun add(tag: String){
        tags.add(tag.toLowerCase())
    }

    fun remove(tag: String){
        tags.remove(tag.toLowerCase())
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

    fun matches(other: Tags): Boolean {
        tags.forEach {
            if (!other.has(it)){
                return false
            }
        }
        other.tags.forEach {
            if (!has(it)){
                return false
            }
        }
        return true
    }
}