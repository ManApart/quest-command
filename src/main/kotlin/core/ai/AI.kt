package core.ai

import core.target.Target

abstract class AI(val name: String, val creature: Target) {

    var aggroTarget: Target? = null

    abstract fun takeAction()

}