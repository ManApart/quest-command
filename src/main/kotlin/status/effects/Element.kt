package status.effects


enum class Element(val getReaction: (strength: Int, other: Element, otherStrength: Int) -> ElementInteraction) {
    AIR({ strength: Int, other: Element, otherStrength: Int ->
        when {
            other == WATER && strength >= otherStrength -> ElementInteraction.STRONGER
            other == WATER && strength < otherStrength -> ElementInteraction.WEAKER
            other == EARTH && strength >= 2 * otherStrength -> ElementInteraction.STRONGER
            other == FIRE && strength >= otherStrength -> ElementInteraction.STRONGER
            other == FIRE && strength < otherStrength -> ElementInteraction.REVERSE_CRITICAL
            else -> ElementInteraction.NONE
        }
    }),

    EARTH({ strength: Int, other: Element, otherStrength: Int ->
        when {
            other in listOf(LIGHTNING, FIRE) && strength >= otherStrength -> ElementInteraction.STRONGER
            other == AIR && strength > otherStrength -> ElementInteraction.CRITICAL
            else -> ElementInteraction.NONE
        }
    }),

    FIRE({ strength: Int, other: Element, otherStrength: Int ->
        when {
            other in listOf(WATER, ICE) && strength >= otherStrength -> ElementInteraction.STRONGER
            other in listOf(WATER, ICE) && strength < otherStrength -> ElementInteraction.WEAKER
            other == AIR && strength > otherStrength -> ElementInteraction.CRITICAL
            else -> ElementInteraction.NONE
        }
    }),

    ICE({ strength: Int, other: Element, otherStrength: Int ->
        when {
            other == FIRE && strength >= otherStrength -> ElementInteraction.STRONGER
            other == FIRE && strength < otherStrength -> ElementInteraction.WEAKER
            other == WATER -> ElementInteraction.CRITICAL
            else -> ElementInteraction.NONE
        }
    }),

    LIGHTNING({ _: Int, other: Element, _: Int ->
        when (other) {
            WATER -> ElementInteraction.CRITICAL
            else -> ElementInteraction.NONE
        }
    }),

    NONE({ _: Int, _: Element, _: Int -> ElementInteraction.NONE }),

    STONE({ _: Int, _: Element, _: Int -> ElementInteraction.NONE }),

    WATER({ strength: Int, other: Element, otherStrength: Int ->
        when {
            other in listOf(FIRE, AIR) && strength >= otherStrength -> ElementInteraction.STRONGER
            other in listOf(FIRE, AIR) && strength < otherStrength -> ElementInteraction.WEAKER
            other == EARTH && strength >= 1.5 * otherStrength -> ElementInteraction.STRONGER
            else -> ElementInteraction.NONE
        }
    });
}