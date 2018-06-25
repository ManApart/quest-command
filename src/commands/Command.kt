package commands

abstract class Command {

    abstract fun getAliases() : Array<String>
    abstract fun getDescription() : String
    abstract fun execute(args: Array<String>)
}