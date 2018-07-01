package commands

class HelpCommand : Command() {
    private var commands: List<Command> = listOf()

    override fun getAliases(): Array<String> {
        return arrayOf("Help")
    }

    override fun execute(args: List<String>) {
        if (args.isEmpty()){
            println(getDescription())
        } else if (args[0] == "commands"){
            printCommands()
        }
    }

    override fun getDescription(): String {
        return "Help: \n\tHelp Commands - Return a list of other commands that can be called"
    }

    fun setCommands(commands: List<Command>){
        this.commands = commands
    }

    private fun printCommands() {
        var descriptions = ""
        commands.forEach { descriptions += it.getDescription() + "\n" }
        print(descriptions)
    }
}