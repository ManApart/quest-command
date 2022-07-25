package core.ai.knowledge.dsl

import core.thing.thing

val cardinal = thing("Bob") {
    props {
        tag("Big", "Red", "Bird", "Prey")
    }
}.build()

val cat = thing("Jerry") {
    props {
        tag("Big", "Blue", "Cat", "Predator")
    }
}.build()

val colorFinders = knowledgeFinders {
    kind({ it in listOf("Big", "Red") }) {
        source({ it.thing?.properties?.tags?.has("Big") == true }) {
            source({ it.thing?.properties?.tags?.has("Red") == true }) {
                fact { _, _, _ -> Opinion(100, 100) }
            }
            source({ it.thing?.properties?.tags?.has("Blue") == true }) {
                fact { _, _, _ -> Opinion(100, 0) }
                listFact { _, _ -> listOf("bird") }
            }
            relatesTo({it.thing?.properties?.tags?.has("Blue") == true}){
                relationship{_,_,_,_ -> Opinion(10, 65)}
            }
        }
        listFact { _, _ -> listOf("Dog") }
    }
}


val relationFinders = knowledgeFinders {
    kind("Eats") {
        source({ it.thing?.properties?.tags?.has("Predator") == true }) {
            relatesTo({ it.thing?.properties?.tags?.has("Prey") == true }) {
                relationship { _, _, _, _ -> Opinion(90, 100) }
            }
        }
    }
    kind("Flees") {
        source({ it.thing?.properties?.tags?.has("Prey") == true }) {
            relatesTo({ it.thing?.properties?.tags?.has("Predator") == true }) {
                relationship { _, _, _, _ -> Opinion(100, 99) }
            }
        }
    }
}