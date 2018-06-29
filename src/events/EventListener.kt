package events

interface EventListener<T : Event> {

    fun handle(event: T)
}