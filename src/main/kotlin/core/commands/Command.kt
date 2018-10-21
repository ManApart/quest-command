package core.commands

abstract class Command {

    abstract fun getAliases() : Array<String>
    abstract fun getDescription() : String
    abstract fun getManual() : String
    abstract fun getCategory() : List<String>
    abstract fun execute(keyword: String, args: List<String>)
}