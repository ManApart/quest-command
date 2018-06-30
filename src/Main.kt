fun main(args: Array<String>) {
    println("Hello World!")
    while (true){
        GameManager.commandParser.parseCommand(readLine() ?: "")
    }
}