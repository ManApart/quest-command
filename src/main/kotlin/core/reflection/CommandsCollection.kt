package core.reflection

import core.commands.Command

interface CommandsCollection {
    val values: List<Command>
}