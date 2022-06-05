package core.events

abstract class EventListener<T : Event> {
    //TODO - move this to just be passed into the execute instead of being a var
    var event: T? = null
    fun execute() {
        if (event != null) {
            execute(this.event!!)
        } else {
            println("$this had a null event, which shouldn't happen!")
        }
    }

    abstract fun execute(event: T)

    open fun shouldExecute(event: T): Boolean {
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