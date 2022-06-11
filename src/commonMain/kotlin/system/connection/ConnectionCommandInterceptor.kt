package system.connection

import core.Player
import core.commands.CommandInterceptor

val ignoredConnectionCommands = listOf("Disconnect", "Connect", "Play", "Be", "Quit")

expect class ConnectionCommandInterceptor : CommandInterceptor {
    override fun ignoredCommands(): List<String>
    override fun parseCommand(source: Player, line: String)

}