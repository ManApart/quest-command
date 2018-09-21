package core.history

object ChatHistory {
    val history = mutableListOf<InputOutput>()
    private var current = InputOutput("Start History")
    private val ignored = mutableListOf<String>()

    fun addInput(input: String) {
        history.add(current)
        current = InputOutput(input)
    }

    fun print(id:String, message: String) {
        current.outPut.add(message)
        if (!ignored.contains(id)){
            println(message)
        }
    }

    fun ignoreMessage(id:String) {
        ignored.add(id)
    }
}