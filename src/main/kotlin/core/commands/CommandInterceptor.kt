package core.commands

import core.thing.Thing

interface CommandInterceptor {
    fun parseCommand(source: Thing, line: String)
    //TODO
//    fun parseCommand(source: Player, line: String)
}