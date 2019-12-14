package status.stat

enum class StatEffect {
    DRAIN, //Each turn reduce current stat by this amount
    DEPLETE, // Only reduce the stat once (when first applied). When the effect is removed, recover the stat by the same amount
    BOOST, // Only increase the stat once (when first applied. Reduce the stat by the same amount when the effect is removed
    RECOVER, // Each turn increase current stat by this amount
    NONE
}