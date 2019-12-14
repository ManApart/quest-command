package magic

import combat.DamageType


enum class Element(val damageType: DamageType, val getReaction: (strength: Int, other: Element, otherStrength: Int) -> ElementInteraction) {
    AIR(DamageType.AIR, { strength: Int, other: Element, otherStrength: Int ->
        when {
            other == WATER && strength >= otherStrength -> ElementInteraction.STRONGER
            other == WATER && strength < otherStrength -> ElementInteraction.WEAKER
            other == EARTH && strength >= 2 * otherStrength -> ElementInteraction.STRONGER
            other == FIRE && strength >= otherStrength -> ElementInteraction.STRONGER
            other == FIRE && strength < otherStrength -> ElementInteraction.REVERSE_CRITICAL
            else -> ElementInteraction.NONE
        }
    }),

    EARTH(DamageType.EARTH, { strength: Int, other: Element, otherStrength: Int ->
        when {
            other in listOf(LIGHTNING, FIRE) && strength >= otherStrength -> ElementInteraction.STRONGER
            other == AIR && strength > otherStrength -> ElementInteraction.CRITICAL
            else -> ElementInteraction.NONE
        }
    }),

    FIRE(DamageType.FIRE, { strength: Int, other: Element, otherStrength: Int ->
        when {
            other in listOf(WATER, ICE) && strength >= otherStrength -> ElementInteraction.STRONGER
            other in listOf(WATER, ICE) && strength < otherStrength -> ElementInteraction.WEAKER
            other == AIR && strength > otherStrength -> ElementInteraction.CRITICAL
            else -> ElementInteraction.NONE
        }
    }),

    ICE(DamageType.ICE, { strength: Int, other: Element, otherStrength: Int ->
        when {
            other == FIRE && strength >= otherStrength -> ElementInteraction.STRONGER
            other == FIRE && strength < otherStrength -> ElementInteraction.WEAKER
            other == WATER -> ElementInteraction.CRITICAL
            else -> ElementInteraction.NONE
        }
    }),

    LIGHTNING(DamageType.LIGHTNING, { _: Int, other: Element, _: Int ->
        when (other) {
            WATER -> ElementInteraction.CRITICAL
            else -> ElementInteraction.NONE
        }
    }),

    NONE(DamageType.NONE, { _: Int, _: Element, _: Int -> ElementInteraction.NONE }),

    STONE(DamageType.STONE, { _: Int, _: Element, _: Int -> ElementInteraction.NONE }),

    WATER(DamageType.WATER, { strength: Int, other: Element, otherStrength: Int ->
        when {
            other in listOf(FIRE, AIR) && strength >= otherStrength -> ElementInteraction.STRONGER
            other in listOf(FIRE, AIR) && strength < otherStrength -> ElementInteraction.WEAKER
            other == EARTH && strength >= 1.5 * otherStrength -> ElementInteraction.STRONGER
            else -> ElementInteraction.NONE
        }
    });
}