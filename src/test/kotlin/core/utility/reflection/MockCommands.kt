package core.utility.reflection

import core.commands.Command
import core.reflection.CommandsCollection

class MockCommands(override val values: List<Command> = listOf()) : CommandsCollection