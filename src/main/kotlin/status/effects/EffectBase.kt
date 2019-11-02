package status.effects

import combat.DamageType
import core.utility.Named

enum class StatKind {LEVELED, PROP_VAL}

data class EffectBase(
        override val name: String,
        val description: String,
        val statTarget: String?,
        val statKind: StatKind = StatKind.LEVELED,
        val statEffect: StatEffect = StatEffect.NONE,
        val amountType: AmountType = AmountType.FLAT_NUMBER,
        val damageType: DamageType = DamageType.NONE
) : Named