package resources.ai.packages

import core.AIPackageStrings.PEASANT
import core.FactKindStrings
import core.GameState
import core.HowToUseStrings
import core.ai.knowledge.setUseTarget
import core.ai.knowledge.useTargetGoal
import core.ai.packages.AIPackageTemplateResource
import core.ai.packages.aiPackages
import core.ai.packages.perceivedActivators
import core.ai.packages.plotRouteAndStartTravel
import core.commands.CommandParsers

class PeasantAIPackage : AIPackageTemplateResource {
    override val values = aiPackages {
        aiPackage(PEASANT) {
            template("Creature")
            idea("Converse") {
                cond { s -> CommandParsers.getConversations().any { it.containsParticipant(s) } }
            }
            idea("Go to Work") {
                cond { s ->
                    val place = s.mind.knownLocationByKind(FactKindStrings.MY_WORKPLACE)
                    GameState.timeManager.isWorkHours() && place != null && s.location != place
                }
                act { s ->
                    s.mind.knownLocationByKind(FactKindStrings.MY_WORKPLACE)?.let { plotRouteAndStartTravel(s, it) }
                }
            }
            idea("Go Home") {
                cond { s ->
                    val place = s.mind.knownLocationByKind(FactKindStrings.MY_HOME)
                    !GameState.timeManager.isWorkHours() && place != null && s.location != place
                }
                act { s ->
                    s.mind.knownLocationByKind(FactKindStrings.MY_HOME)?.let { plotRouteAndStartTravel(s, it) }
                }
            }
            idea("Work", takesTurn = false) {
                cond { s ->
                    GameState.timeManager.isWorkHours()
                            && s.location == s.mind.knownLocationByKind(FactKindStrings.MY_WORKPLACE)
                            && (s.mind.knows(FactKindStrings.WORK_TAGS)?.sources?.mapNotNull { it.topic }?.isNotEmpty() ?: false)
                }
                act { s ->
                    s.mind.knows(FactKindStrings.WORK_TAGS)?.sources?.mapNotNull { it.topic }?.let { tags ->
                        s.perceivedActivators().firstOrNull { it.properties.tags.hasAll(tags) }?.let { s.setUseTarget(it, HowToUseStrings.INTERACT) }
                    }
                }
            }
            idea("Want to Sleep in Bed", takesTurn = false) {
                cond { s ->
                    GameState.timeManager.isNight() && s.mind.knownThingByKind(FactKindStrings.MY_BED) != null && s.useTargetGoal() != HowToUseStrings.SLEEP.lowercase()
                }
                act { s -> s.mind.knownThingByKind(FactKindStrings.MY_BED)?.let { s.setUseTarget(it, HowToUseStrings.SLEEP) } }
            }
        }
    }
}
