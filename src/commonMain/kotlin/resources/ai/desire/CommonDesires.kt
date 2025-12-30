package resources.ai.desire

import core.GameState
import core.ai.desire.DesireResource
import core.ai.desire.desires
import core.ai.packages.perceivedActivators
import core.ai.packages.perceivedCreatures
import core.ai.packages.perceivedItems
import core.commands.CommandParsers
import status.stat.STAMINA

class CommonDesires : DesireResource {
    override suspend fun values() = desires {
        agenda("Nothing")
        agenda("Wander")

        cond({ s -> s.soul.getCurrent(STAMINA) < s.soul.getTotal(STAMINA) / 10 }) {
            agenda("Rest")
        }

        cond({ source -> source.mind.getAggroTarget() != null }) {
            priority = 70
            agenda("Attack")
        }

        tag("Commoner") {
            cond({ _ -> GameState.timeManager.getPercentDayComplete() in listOf(25, 50, 75) }) {
                additionalPriority = 2
                agenda("Eat Food")
            }
            cond({ s -> CommandParsers.getConversations().any { it.containsParticipant(s) } }) {
                priority = 65
                agenda("Converse")
            }

            cond({ s -> GameState.timeManager.isWorkHours() && s.mind.locationByKindExists("MyWorkplace") }) {
                cond({ s -> s.location != s.mind.knownLocationByKind("MyWorkplace") }) {
                    agenda("Travel to Job Site")
                }
                agenda("Work At Job Site")
            }

            cond({ _ -> !GameState.timeManager.isNight() }) {
                cond({ s -> s.mind.thingByKindExists("MyBed") }) {
                    agenda("Sleep In Bed")
                }
                agenda("Rest")
            }
        }

        tag("Predator") {
            cond({ _ -> !GameState.timeManager.isNight() }) {
                agenda("Rest")
            }

            cond({ s -> s.perceivedItems().firstOrNull { it.properties.tags.has("Food") } != null }) {
                agenda("Eat Food")
            }

            cond(20, { s -> s.perceivedActivators().firstOrNull { it.name.contains("Tree") } != null }) {
                agenda("Scratch Tree")
            }
            //Eventually use factions + actions to create how much something likes something else
            cond({ s -> s.perceivedCreatures().firstOrNull { !it.properties.tags.has("Predator") } != null }) {
                additionalPriority = 10
                agenda("Hunt")
            }
        }
    }
}
