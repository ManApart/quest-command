package resources.ai.packages

import core.ai.packages.AIPackageTemplateResource
import core.ai.packages.aiPackages

class CommonerPackage : AIPackageTemplateResource {
    override val values = aiPackages {
        aiPackage("Commoner") {
            template("Creature")
            idea("Eat Food"){
                //TODO
            }
            idea("Converse"){
                //TODO
            }
            idea("Travel to Job Site"){
                //TODO
            }
            idea("Work"){
                //TODO
            }
            idea("Sleep in Bed"){
                //TODO
            }
            idea("Rest"){
                //TODO
            }
        }
    }
}
