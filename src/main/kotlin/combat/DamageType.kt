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

    val verb get() = name.toLowerCase() + "es"
    val verbPlural get() = name.toLowerCase()
    val damage get() = name.toLowerCase() + "Damage"
    val health get() = name.toLowerCase() + "Health"
    val defense get() = name.toLowerCase() + "Defense"
}