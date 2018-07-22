package system

import core.events.EventListener
import core.gameState.GameState

object GameManager {

    fun saveGame() {
        TODO("not implemented") //To change body of created functions interact File | Settings | File Templates.
    }

    fun loadGame() {
        TODO("not implemented") //To change body of created functions interact File | Settings | File Templates.
    }

    fun newGame() {
        newPlayer()
    }

    private fun newPlayer() {
        val items = GameState.player.creature.inventory.items
        val body = GameState.player.creature.body

        listOf("Brown Pants", "Old Shirt", "Rusty Dagger").forEach {
            val item = ItemManager.getItem(it)
            items.add(item)
            body.equip(item)
        }
        items.add(ItemManager.getItem("Dulled Hatchet"))
        items.add(ItemManager.getItem("Apple"))
    }

    class MessageHandler() : EventListener<MessageEvent>() {
        override fun execute(event: MessageEvent) {
            println(event.message)
        }

    }

}