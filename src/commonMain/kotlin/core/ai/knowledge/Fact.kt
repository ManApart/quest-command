package core.ai.knowledge

import core.properties.Properties

data class Fact(val source: Subject, val kind: String, val props: Properties = Properties())

data class ListFact(val kind: String, val sources: List<Subject>, val props: Properties = Properties()) {
    constructor(kind: String, source: Subject, props: Properties = Properties()) : this(kind, listOf(source), props)
}

enum class FactKind {
    AGGRO_TARGET,
    HOME,
    LOCATION,
    LOCATION_GOAL,
    MY_BED,
    MY_HOME,
    MY_WORKPLACE,
    RECIPE,
    USE_TARGET,
    WORK_TAGS,
}

enum class HowToUse {
    EAT,
    ATTACK,
    SLEEP,
    INTERACT,
}
