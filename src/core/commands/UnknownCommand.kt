package core.commands

class UnknownCommand : Command() {

    override fun getAliases(): Array<String> {
        return arrayOf("")
    }

    override fun getDescription(): String {
        return "Called when no other command is present"
    }

    override fun getManual(): String {
        return "This is the manual for an Unknown Command"
    }

    override fun getCategory(): List<String> {
        return listOf("")
    }

    override fun execute(args: List<String>) {
        println("Unknown command: ${args.joinToString(" ")}")
    }
}