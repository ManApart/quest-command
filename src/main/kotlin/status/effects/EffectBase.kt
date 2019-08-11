package status.effects

import combat.DamageType
import core.utility.Named

data class EffectBase(
        override val name: String,
        val description: String,
        val statTarget: String,
        val statEffect: StatEffect,
        val amountType: AmountType = AmountType.FLAT_NUMBER,
        val damageType: DamageType = DamageType.NONE
) : Named