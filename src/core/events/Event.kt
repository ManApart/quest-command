package core.events

interface Event {
    fun usesGameTick() : Boolean {
        return true
    }
}