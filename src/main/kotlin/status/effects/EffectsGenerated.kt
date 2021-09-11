package status.effects

class EffectsGenerated : EffectsCollection {
    override val values = listOf<EffectResource>(resources.status.CommonEffects()).flatMap { it.values }
}