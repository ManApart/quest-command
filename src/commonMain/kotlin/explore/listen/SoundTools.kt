package explore.listen

import combat.DamageType
import core.TagKey.SOUND_LEVEL
import core.thing.Thing
import magic.Element
import status.conditions.Condition
import status.effects.Effect
import status.effects.EffectBase
import status.stat.AmountType
import status.stat.StatEffect
import status.stat.StatKind

fun Thing.addSoundEffect(name: String, description: String, amount: Int = SOUND_LEVEL_DEFAULT, duration: Int = 2) {
    soul.addNewCondition(soundCondition(name, description, amount, duration))
}

fun soundCondition(name: String, description: String, amount: Int, duration: Int = 2): Condition{
    return Condition("Sound:$name", Element.SOUND, 1, listOf(soundEffect(name, description, amount, duration)), silent = true)
}

fun soundEffect(name: String, description: String, amount: Int, duration: Int): Effect  {
    val base = EffectBase(name, description, SOUND_LEVEL, StatKind.PROP_VAL, StatEffect.BOOST, AmountType.FLAT_NUMBER, DamageType.NONE)
    return Effect(base, amount, duration)
}
