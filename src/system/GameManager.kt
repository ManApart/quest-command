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
        val items = GameState.player.inventory.items
        items.add(ItemManager.getItem("Brown Pants"))
        items.add(ItemManager.getItem("Old Shirt"))
        items.add(ItemManager.getItem("Rusty Dagger"))
        items.add(ItemManager.getItem("Dulled Hatchet"))
        items.add(ItemManager.getItem("Apple"))
    }

    class MessageHandler() : EventListener<MessageEvent>() {
        override fun execute(event: MessageEvent) {
            println(event.message)
        }

    }

}