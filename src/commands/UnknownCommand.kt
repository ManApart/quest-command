package commands

class UnknownCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("")
    }

    override fun getDescription(): String {
        return "Called when no other command is present"
    }

    override fun execute(args: Array<String>) {
        print("Unknown command: ${args.joinToString(" ")}")
    }
}