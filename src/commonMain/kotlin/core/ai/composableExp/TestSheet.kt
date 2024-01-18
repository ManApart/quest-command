package core.ai.composableExp

import status.rest.RestEvent
import use.eat.EatFoodEvent

fun testAiPackages() {
    aiPackages {
        aiPackage("animal") {
            criteria({ !it.isSafe() }) {
                criteria({ !it.isSafe() }) {
                    idea("eat") {
                        criteria { !it.isSafe() }
                        action { EatFoodEvent(it, it) }
                    }
                    idea("wait") {
                        criteria { !it.isSafe() }
                        action { EatFoodEvent(it, it) }
                    }
                }
            }
        }
        aiPackage("predator") {
            template("animal")
            idea("hunt") {
                criteria { it.soul.getConditions().isEmpty() }
                action { RestEvent(it, 1) }
            }
        }
    }
}