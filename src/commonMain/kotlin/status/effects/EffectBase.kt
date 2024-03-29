package status.effects

import combat.DamageType
import core.utility.Named
import status.stat.AmountType
import status.stat.StatEffect
import status.stat.StatKind

@kotlinx.serialization.Serializable
data class EffectBase(
        override val name: String,
        val description: String,
        val statThing: String? = null,
        val statKind: StatKind = StatKind.LEVELED,
        val statEffect: StatEffect = StatEffect.NONE,
        val amountType: AmountType = AmountType.FLAT_NUMBER,
        val damageType: DamageType = DamageType.NONE
) : Named