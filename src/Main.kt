fun main(args: Array<String>) {
    while (true){
        GameManager.commandParser.parseCommand(readLine() ?: "")
    }
}