package core.history

fun display(message: String){
    ChatHistory.print("NotUsedYet", message)
}

object ChatHistory {
    val history = mutableListOf<InputOutput>()
    private var current = InputOutput()
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

    fun reset() {
        history.clear()
        current = InputOutput()
    }

    fun getLastInput() : String {
        return  current.input
    }

    fun getLastOutput() : String {
        return current.outPut.lastOrNull() ?: ""
    }

    fun getLastOutputs() : List<String> {
        return current.outPut
    }

}