package core.events

abstract class EventListener<T : Event> {
    var event: T? = null
    fun execute(){
        if (event != null){
            execute(this.event!!)
        } else {
            println("$this had a null event, which shouldn't happen!")
        }
    }
    abstract fun execute(event: T)

    open fun shouldExecute(event: T) : Boolean {
        return true
    }

    open fun getPriority() : Int {
        return 0
    }

}