package resources.status

import combat.DamageType
import explore.listen.SOUND_LEVEL
import status.effects.EffectResource
import status.effects.effects
import status.stat.StatEffect
import status.stat.StatKind
import traveling.scope.LIGHT

class CommonEffects : EffectResource {
    override val values = effects {
        statThing("Health") {
            effectType(StatEffect.DRAIN) {
                effect("Chopped", DamageType.CHOP, "Chopped by a blow.")
                effect("Crushed", DamageType.CRUSH, "Crushed by a blow.")
                effect("Slashed", DamageType.SLASH, "Slashed by a blow.")
                effect("Stabbed", DamageType.STAB, "Stabbed by a blow.")
            }
        }

        effect("Air Blasted") {
            description("Blown with a heavy blast of air.")
            air()
        }

        damageType(DamageType.EARTH) {
            effect("Encased Agility") {
                description("Encased in a thick layer of dirt.")
                deplete()
                leveled("Agility")
            }
            effect("Dirty") {
                description("Covered in Earth.")
            }
            effect("Encased Encumbrance") {
                description("Encased in a thick layer of dirt.")
                boost()
                propertyValue("PROP_VAL")
            }
            effect("Encased Slash Defense") {
                description("Encased in a thick layer of dirt.")
                boost()
                propertyValue("slashDefense")
            }
            effect("Encased Slash Defense") {
                description("Encased in a thick layer of dirt.")
                boost()
                propertyValue("chopDefense")
            }
        }

        damageType(DamageType.FIRE) {
            effect("Burning") {
                description("Flames linger and lick, curling into the air.")
                drain()
                leveled("Health")
            }
            effect("On Fire") {
                description("Flames linger and lick, curling into the air.")
                drain()
                propertyValue("fireHealth")
            }
            effect("Lit") {
                description("Red and yellow light dance together.")
                boost()
                propertyValue(LIGHT)
            }
            effect("Warm") {
                description("Increase the level of heat.")
                boost()
                propertyValue("Heat")
            }
        }

        damageType(DamageType.ICE) {
            effect("Cool") {
                description("Decrease the level of heat.")
                deplete()
                propertyValue("Heat")
            }
            effect("Frozen") {
                description("Encased in a thick layer of ice.")
            }
        }

        effect("Shocked") {
            description("Current rushes across muscle and tendon.")
            lightning()
        }

        effect("Stunned") {
            description("Consciousness is lost for a second.")
            deplete()
            leveled("Agility")
        }

        damageType(DamageType.WATER) {
            effect("Wet") {
                description("Dripping wet.")
                deplete()
                leveled("Agility")
            }
            effect("Watery Slice") {
                description("Water slices and smashes.")
                deplete()
                leveled("Agility")
            }
            effect("Water Slice") {
                description("Water slices and smashes.")
                drain()
                leveled("Health")
            }
            effect("Heal") {
                description("Rejuvenate and restore health.")
                recover()
                leveled("Health")
            }
            effect("Poison") {
                description("Lose health over time.")
                drain()
                leveled("Health")
            }
        }

        statThing(LIGHT) {
            statKind(StatKind.PROP_VAL) {
                effect("Brighten") {
                    description("Light is cast on this.")
                    boost()
                }
                effect("Darken") {
                    description("Shadow is cast on this.")
                    deplete()
                }
            }
        }

        effect("Talking"){
            description("You hear them talking.")
            boost()
            propertyValue(SOUND_LEVEL)
        }


    }
}