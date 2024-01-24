package resources.ai.packages

import core.ai.packages.AIPackageTemplateResource
import core.ai.packages.aiPackages
import status.rest.RestEvent
import use.eat.EatFoodEvent

class CommonPackages: AIPackageTemplateResource {
    override val values =  aiPackages {
        aiPackage("animal") {
            criteria({ !it.isSafe() }) {
                criteria({ !it.isSafe() }) {
                    idea("eat", 10) {
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
            priority("animal-eat", 15)
            drop("animal-wait")

            idea("hunt") {
                criteria { it.soul.getConditions().isEmpty() }
                action { RestEvent(it, 1) }
            }
        }
    }

}