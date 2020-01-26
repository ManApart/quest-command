package core.history

data class InputOutput(val input: String = "Start History", val outPut: MutableList<String> = mutableListOf(), var timeTaken: Long = 0)