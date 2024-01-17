package core.ai.composableExp

import use.eat.EatFoodEvent

fun testAiPackages() {
    aiPackages {
        aiPackage("animal") {
            idea("eat") {
                criteria { !it.isSafe() }
                action { EatFoodEvent(it, it) }
            }
        }
        aiPackage("predator") {
            template("animal")
        }
    }
}