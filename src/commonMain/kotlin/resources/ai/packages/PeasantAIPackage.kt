package resources.ai.packages

import core.AIPackageKeys.PEASANT
import core.FactKind
import core.GameState
import core.HowToUse
import core.ai.knowledge.setUseTarget
import core.ai.packages.AIPackageTemplateResource
import core.ai.packages.aiPackages
import core.ai.packages.canReachGoal
import core.ai.packages.perceivedActivators
import core.ai.packages.plotRouteAndStartTravel
import core.commands.CommandParsers

class PeasantAIPackage : AIPackageTemplateResource {
    override val values = aiPackages {
        aiPackage(PEASANT) {
            template("Creature")
            idea("Converse") {
                cond { s -> CommandParsers.getConversations().any { it.containsParticipant(s) }}
            }
            idea("Go to Work") {
                cond { s ->
                    val place = s.mind.knownLocationByKind(FactKind.MY_WORKPLACE)
                    GameState.timeManager.isWorkHours() && place != null && s.location != place
                }
                act { s ->
                    s.mind.knownLocationByKind(FactKind.MY_WORKPLACE)?.let { plotRouteAndStartTravel(s, it) }
                }
            }
            idea("Go Home") {
                cond { s ->
                    val place = s.mind.knownLocationByKind(FactKind.MY_HOME)
                    !GameState.timeManager.isWorkHours() && place != null && s.location != place
                }
                act { s ->
                    s.mind.knownLocationByKind(FactKind.MY_HOME)?.let { plotRouteAndStartTravel(s, it) }
                }
            }
            idea("Work", takesTurn = false) {
                cond { s ->
                    GameState.timeManager.isWorkHours()
                            && s.location == s.mind.knownLocationByKind(FactKind.MY_WORKPLACE)
                            && (s.mind.knows(FactKind.WORK_TAGS)?.sources?.mapNotNull { it.topic }?.isNotEmpty() ?: false)
                }
                act { s ->
                    s.mind.knows(FactKind.WORK_TAGS)?.sources?.mapNotNull { it.topic }?.let { tags ->
                        s.perceivedActivators().firstOrNull { it.properties.tags.hasAll(tags) }?.let { s.setUseTarget(it, HowToUse.INTERACT) }
                    }
                }
            }
            idea("Want to Sleep in Bed", takesTurn = false) {
                cond { s -> GameState.timeManager.isNight() && s.mind.knownThingByKind(FactKind.MY_BED) != null }
                act { s -> s.mind.knownThingByKind(FactKind.MY_BED)?.let { s.setUseTarget(it, HowToUse.SLEEP) } }
            }
            idea("Sleep in Bed") {
                cond { s ->
                    GameState.timeManager.isNight() && s.canReachGoal(HowToUse.SLEEP)
                }

            }
        }
    }
}
