fun main(args: Array<String>) {
    println("Hello World!")
    val commandParser = CommandParser()
    while (true){
        commandParser.parseCommand(readLine() ?: "")
    }
}