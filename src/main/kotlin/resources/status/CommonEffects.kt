package resources.status

import combat.DamageType
import status.effects.EffectBase
import status.effects.EffectResource
import status.stat.StatEffect
import status.stat.StatKind
import traveling.scope.LIGHT

class CommonEffects : EffectResource {
    override val values = listOf(
        EffectBase("Chopped", "Chopped by a blow.", "Health", statEffect = StatEffect.DRAIN, damageType = DamageType.CHOP),
        EffectBase("Crushed", "Crushed by a blow.", "Health", statEffect = StatEffect.DRAIN, damageType = DamageType.CRUSH),
        EffectBase("Slashed", "Slashed by a blow.", "Health", statEffect = StatEffect.DRAIN, damageType = DamageType.SLASH),
        EffectBase("Stabbed", "Stabbed by a blow.", "Health", statEffect = StatEffect.DRAIN, damageType = DamageType.STAB),
        EffectBase("Air Blasted", "Blown with a heavy blast of air.", damageType = DamageType.STAB),
        EffectBase("Encased Agility", "Encased in a thick layer of dirt.", "Agility", statEffect = StatEffect.DEPLETE, damageType = DamageType.EARTH),
        EffectBase("Dirty", "Covered in Earth.", damageType = DamageType.EARTH),
        EffectBase("Encased Encumbrance", "Encased in a thick layer of dirt.", "PROP_VAL", StatKind.PROP_VAL, StatEffect.BOOST, damageType = DamageType.EARTH),
        EffectBase("Encased Slash Defense", "Encased in a thick layer of dirt.", "slashDefense", StatKind.PROP_VAL, StatEffect.BOOST, damageType = DamageType.EARTH),
        EffectBase("Encased Chop Defense", "Encased in a thick layer of dirt.", "chopDefense", StatKind.PROP_VAL, StatEffect.BOOST, damageType = DamageType.EARTH),
        EffectBase("Burning", "Flames linger and lick, curling into the air.", "Health", statEffect= StatEffect.DRAIN, damageType = DamageType.FIRE),
        EffectBase("On Fire", "Flames linger and lick, curling into the air.", "fireHealth", StatKind.PROP_VAL, StatEffect.DRAIN, damageType = DamageType.FIRE),
        EffectBase("Lit", "Red and yellow light dance together.", LIGHT, StatKind.PROP_VAL, StatEffect.BOOST, damageType = DamageType.FIRE),
        EffectBase("Frozen", "Encased in a thck layer of ice.", damageType = DamageType.ICE),
        EffectBase("Shocked", "Current rushes across muscle and tendon.", damageType = DamageType.LIGHTNING),
        EffectBase("Stunned", "Consciousness is lost for a second.", "Agility", statEffect = StatEffect.DEPLETE),
        EffectBase("Wet", "Dripping wet.", "Agility", statEffect = StatEffect.DEPLETE, damageType = DamageType.WATER),
        EffectBase("Water Slice", "Water slices and smashes", "Agility", statEffect = StatEffect.DEPLETE, damageType = DamageType.WATER),
        EffectBase("Water Slice", "Water slices and smashes", "Health", statEffect = StatEffect.DRAIN, damageType = DamageType.WATER),
        EffectBase("Heal", "Rejuvenate and restore health.", "Health", statEffect = StatEffect.RECOVER, damageType = DamageType.WATER),
        EffectBase("Poison", "Lose health over time.", "Health", statEffect = StatEffect.DRAIN, damageType = DamageType.WATER),
        EffectBase("Warm", "Increase the level of heat.", "Heat", statEffect = StatEffect.BOOST, damageType = DamageType.FIRE),
        EffectBase("Cool", "Decrease the level of heat.", "Heat", statEffect = StatEffect.DEPLETE, damageType = DamageType.ICE),
        EffectBase("Brighten", "Light is cast on this.", "Light", StatKind.PROP_VAL, statEffect = StatEffect.BOOST),
        EffectBase("Darken", "Shadow is cast on this.", "Light", StatKind.PROP_VAL, statEffect = StatEffect.DEPLETE),
        )

}