package core.ai.knowledge.dsl

import core.thing.thing

val cardinal = thing("Bob") {
    props {
        tag("Big", "Red", "Bird")
    }
}.build()

val cat = thing("Jerry") {
    props {
        tag("Big", "Blue", "Cat")
    }
}.build()

val finders = knowledgeFinders {
    kind({ it in listOf("Big", "Red") }) {
        source({ it.thing?.properties?.tags?.has("Big") == true }) {
            source({ it.thing?.properties?.tags?.has("Red") == true }) {
                fact { _, _, _ -> Opinion(100, 100) }
            }
            source({ it.thing?.properties?.tags?.has("Blue") == true }) {
                fact { _, _, _ -> Opinion(100, 0) }
            }
        }
        listFact { _, _ -> listOf("Dog") }
    }
}
