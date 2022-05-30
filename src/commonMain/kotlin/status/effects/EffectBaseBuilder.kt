package status.effects

import combat.DamageType
import status.stat.AmountType
import status.stat.StatEffect
import status.stat.StatKind

class EffectBaseBuilder(
    private val name: String,
    private var statThing: String? = null,
    private var statEffect: StatEffect = StatEffect.NONE,
    private var damageType: DamageType = DamageType.NONE,
    private var statKind: StatKind = StatKind.LEVELED,
) {
    private var amountType: AmountType = AmountType.FLAT_NUMBER
    private var description: String = ""

    fun build(): EffectBase {
        return EffectBase(name, description, statThing, statKind, statEffect, amountType, damageType)
    }

    fun statThing(statThing: String) {
        this.statThing = statThing
    }

    fun statEffect(effect: StatEffect) {
        this.statEffect = effect
    }

    fun drain() = run { this.statEffect = StatEffect.DRAIN }
    fun deplete() = run { this.statEffect = StatEffect.DEPLETE }
    fun boost() = run { this.statEffect = StatEffect.BOOST }
    fun recover() = run { this.statEffect = StatEffect.RECOVER }

    fun damageType(statThing: String) {
        this.statThing = statThing
    }

    fun air() = run { this.damageType = DamageType.AIR }
    fun earth() = run { this.damageType = DamageType.EARTH }
    fun chop() = run { this.damageType = DamageType.CHOP }
    fun crush() = run { this.damageType = DamageType.CRUSH }
    fun fire() = run { this.damageType = DamageType.FIRE }
    fun ice() = run { this.damageType = DamageType.ICE }
    fun lightning() = run { this.damageType = DamageType.LIGHTNING }
    fun slash() = run { this.damageType = DamageType.SLASH }
    fun stone() = run { this.damageType = DamageType.STONE }
    fun stab() = run { this.damageType = DamageType.STAB }
    fun water() = run { this.damageType = DamageType.WATER }

    fun statKind(statKind: StatKind) {
        this.statKind = statKind
    }

    fun propertyValue(propertyName: String) {
        this.statKind = StatKind.PROP_VAL
        this.statThing = propertyName
    }

    fun leveled(propertyName: String) {
        this.statKind = StatKind.LEVELED
        this.statThing = propertyName
    }

    fun amountType(type: AmountType) {
        this.amountType = type
    }

    fun flatNumber() = run { this.amountType = AmountType.FLAT_NUMBER }
    fun percent() = run { this.amountType = AmountType.PERCENT }

    fun description(description: String) {
        this.description = description
    }

}

fun effect(name: String, initializer: EffectBaseBuilder.() -> Unit): EffectBase {
    return EffectBaseBuilder(name).apply(initializer).build()
}