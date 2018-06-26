package commands

class HelpCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Help")
    }

    override fun execute(args: List<String>) {
        if (args.isEmpty()){
            println(getDescription())
        }
    }

    override fun getDescription(): String {
        return "Get Help on other commands"
    }
}