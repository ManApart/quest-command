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
        GameState.player.items.add(GameState.getItem("Brown Pants"))
        GameState.player.items.add(GameState.getItem("Old Shirt"))
        GameState.player.items.add(GameState.getItem("Rusty Dagger"))
    }

}