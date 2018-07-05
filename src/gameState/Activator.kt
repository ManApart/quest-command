package gameState

class Activator(val name: String, val description: String = "", tags: List<String> = listOf()) : Target {
    override val tags = Tags(tags)
}