package core.events

interface Event {
    fun gameTicks(): Int = 0
    fun isExecutableByAI(): Boolean = false
}