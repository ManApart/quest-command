package system.debug

enum class DebugType(val propertyName: String) {
    DEBUG_GROUP("Debug Group"),
    LEVEL_REQ("Ignore Level Requirements"),
    STAT_CHANGES("Ignore Stat Changes"),
    RANDOM("Ignore Random")
}