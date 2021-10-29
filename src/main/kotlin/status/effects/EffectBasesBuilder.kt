package status.effects

import combat.DamageType
import status.stat.StatEffect
import status.stat.StatKind

class EffectBasesBuilder(
    private var statThing: String? = null,
    private var statEffect: StatEffect = StatEffect.NONE,
    private var damageType: DamageType = DamageType.NONE,
    private var statKind: StatKind = StatKind.LEVELED,
) {
    private val effectBuilders = mutableListOf<EffectBaseBuilder>()

    fun build(): List<EffectBase>{
        return effectBuilders.map { it.build() }
    }

    fun effect(name: String, initializer: EffectBaseBuilder.() -> Unit = {}) {
        effectBuilders.add(EffectBaseBuilder(name, statThing, statEffect, damageType, statKind).apply(initializer))
    }

    fun effect(name: String, damageType: DamageType, description: String, initializer: EffectBaseBuilder.() -> Unit = {}) {
        effectBuilders.add(EffectBaseBuilder(name, statThing, statEffect, damageType, statKind).apply { description(description) }.apply(initializer))
    }

    fun effectType(statEffect: StatEffect, initializer: EffectBasesBuilder.() -> Unit){
        effectBuilders.addAll(EffectBasesBuilder(statThing, statEffect, damageType, statKind).apply(initializer).effectBuilders)
    }

    fun statKind(statKind: StatKind, initializer: EffectBasesBuilder.() -> Unit){
        effectBuilders.addAll(EffectBasesBuilder(statThing, statEffect, damageType, statKind).apply(initializer).effectBuilders)
    }

    fun damageType(damageType: DamageType, initializer: EffectBasesBuilder.() -> Unit){
        effectBuilders.addAll(EffectBasesBuilder(statThing, statEffect, damageType).apply(initializer).effectBuilders)
    }

    fun statThing(statThing: String, initializer: EffectBasesBuilder.() -> Unit){
        effectBuilders.addAll(EffectBasesBuilder(statThing, statEffect, damageType).apply(initializer).effectBuilders)
    }
}

fun effects(initializer: EffectBasesBuilder.() -> Unit): List<EffectBase> {
    return EffectBasesBuilder().apply(initializer).build()
}