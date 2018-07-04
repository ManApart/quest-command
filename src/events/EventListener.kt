package events

abstract class EventListener<T : Event> {

    abstract fun handle(event: T)
    open fun getPriority() : Int {
        return 0
    }

}