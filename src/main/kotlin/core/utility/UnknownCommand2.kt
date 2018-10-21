package core.utility

import core.commands.Command

class UnknownCommand2 : Command() {

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

    fun execute(args: List<String>) {
        execute("", args)
    }

    override fun execute(keyword: String, args: List<String>) {
        println("Unknown command: ${args.joinToString(" ")}")
    }
}