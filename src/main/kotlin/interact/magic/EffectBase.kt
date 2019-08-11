package interact.magic

import combat.DamageType

data class EffectBase(
        val name: String,
        val description: String,
        val statEffect: StatEffect,
        val amountType: AmountType,
        val damageType: DamageType,
        val statTarget: String
)