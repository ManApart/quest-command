package gameState

class Properties(properties: Map<String, String> = HashMap()) {
    private val properties = parseProperties(properties)

    private fun parseProperties(properties: Map<String, String>): Map<String, String> {
        val parsed = HashMap<String, String>()
        properties.entries.forEach {
            parsed[it.key.toLowerCase()] = it.value.toLowerCase()
        }
        return parsed
    }

    fun hasProperty(key: String): Boolean {
        return properties.containsKey(key.toLowerCase())
    }

    fun getInt(key: String): Int {
        if (properties.containsKey(key.toLowerCase())) {
            return Integer.parseInt(properties[key.toLowerCase()])
        }
        return 0
    }

    fun getString(key: String): String {
        return properties[key.toLowerCase()] ?: ""
    }
}