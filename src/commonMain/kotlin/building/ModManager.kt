package building

import core.thing.ThingBuilder

object ModManager {
    val itemBuilders = mutableListOf<ThingBuilder>()

    fun reset(){
        itemBuilders.clear()
    }
}