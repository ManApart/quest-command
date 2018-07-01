package events

interface EventListener<T : Event> {

    fun getPriority() : Int
    fun handle(event: T)
}