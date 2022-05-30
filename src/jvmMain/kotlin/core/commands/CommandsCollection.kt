package core.commands
import core.commands.Command

interface CommandsCollection {
    val values: List<Command>
}