package core.gameState.ai

import core.gameState.Target

abstract class AI(val name: String, val creature: Target) {

    abstract fun takeAction()

}