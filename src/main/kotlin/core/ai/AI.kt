package core.ai

import core.target.Target

abstract class AI(val name: String, val creature: Target) {

    abstract fun takeAction()

}