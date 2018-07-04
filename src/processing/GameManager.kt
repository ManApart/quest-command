package processing

import gameState.GameState

object GameManager {

    fun saveGame(){
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun loadGame(){
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun newGame() {
        newPlayer()
    }

    private fun newPlayer() {
        GameState.player.items.add(ItemManager.getItem("Brown Pants"))
        GameState.player.items.add(ItemManager.getItem("Old Shirt"))
        GameState.player.items.add(ItemManager.getItem("Rusty Dagger"))
        GameState.player.items.add(ItemManager.getItem("Apple"))
        GameState.player.items.add(ItemManager.getItem("Apple"))
    }

}