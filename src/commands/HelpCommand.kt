package commands

class HelpCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Help")
    }

    override fun execute(args: Array<String>) {
        if (args.isEmpty()){
            print(getDescription())
        }
    }

    override fun getDescription(): String {
        return "Get Help on other commands"
    }
}