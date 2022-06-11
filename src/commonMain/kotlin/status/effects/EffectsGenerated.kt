package status.effects

class EffectsGenerated : EffectsCollection {
    override val values by lazy { listOf<EffectResource>(resources.status.CommonEffects()).flatMap { it.values }}
}