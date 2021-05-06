package combat

enum class DamageType {
    AIR,
    EARTH,
    CHOP,
    CRUSH,
    FIRE,
    ICE,
    LIGHTNING,
    NONE,
    SLASH,
    STONE,
    STAB,
    WATER;

    val verb
        get() = if (this == CHOP) {
            name.lowercase()
        } else {
            name.lowercase() + "es"
        }

    val verbPlural get() = name.lowercase()
    val damage get() = name.lowercase() + "Damage"
    val health get() = name.lowercase() + "Health"
    val defense get() = name.lowercase() + "Defense"
}