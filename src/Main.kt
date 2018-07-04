fun main(args: Array<String>) {
    while (true){
        CommandParser.parseCommand(readLine() ?: "")
    }
}