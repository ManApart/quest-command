package core.conditional

class StringOption(val option: String, val condition: () -> Boolean = { true })