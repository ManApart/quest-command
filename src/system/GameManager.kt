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
        with(GameState.player.inventory.items) {
            add(ItemManager.getItem("Brown Pants"))
            add(ItemManager.getItem("Old Shirt"))
            add(ItemManager.getItem("Rusty Dagger"))
            add(ItemManager.getItem("Dulled Hatchet"))
            add(ItemManager.getItem("Apple"))
        }
    }

    class MessageHandler() : EventListener<MessageEvent>(){
        override fun execute(event: MessageEvent) {
            println(event.message)
        }

    }

}