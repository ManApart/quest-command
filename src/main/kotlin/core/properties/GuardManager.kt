package core.properties

object GuardManager {
    private val guards by lazy { buildGuardMap() }

    private fun buildGuardMap(): Map<String, (String, String) -> String> {
        return mapOf(
                FIRE_HEALTH to ::isGreaterThanEqualToZero
        )
    }

    fun getGuardedValue(key: String, current: String, newValue: String): String {
        val lowKey = key.toLowerCase()
        val guard = guards[lowKey]
        return guard?.invoke(current, newValue) ?: newValue
    }

}