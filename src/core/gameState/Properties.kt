package core.gameState

class Properties(properties: Map<String, String> = HashMap()) {
    private val properties = parseProperties(properties)

    private fun parseProperties(properties: Map<String, String>): Map<String, String> {
        val parsed = HashMap<String, String>()
        properties.entries.forEach {
            parsed[it.key.toLowerCase()] = it.value.toLowerCase()
        }
        return parsed
    }

    fun getInt(key: String, default: Int = 0): Int {
        if (properties.containsKey(key.toLowerCase())) {
            return Integer.parseInt(properties[key.toLowerCase()])
        }
        return default
    }

    fun getString(key: String, default: String = ""): String {
        return properties[key.toLowerCase()] ?: default
    }
}