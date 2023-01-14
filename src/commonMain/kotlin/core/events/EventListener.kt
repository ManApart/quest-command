package core.events

abstract class EventListener<T : Event> {
    abstract suspend fun execute(event: T)

    open suspend fun shouldExecute(event: T): Boolean {
        return true
    }

    open fun reset() {}

    /**
     * The lower the priority rank, the sooner it will execute (sorted against others listening for the same type of events)
     */
    open fun getPriorityRank(): Int {
        return 0
    }

}