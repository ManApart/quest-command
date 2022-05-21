package core.ai.knowledge

import core.thing.Thing
import traveling.location.network.LocationNode

enum class Confidence(val amount: Int){ UNKNOWN(0), GUESS(25), ESTIMATE(50), CONFIDENT(75), POSITIVE(100)}

fun Int.confidence(): Confidence {
    return Confidence.values().filter { it.amount < this }.maxOrNull()!!
}

data class Fact(val source: Subject, val kind: String, val confidence: Int, val amount: Int = 0) {
    operator fun plus(other: Fact): Fact {
        return Fact(source, kind, confidence + other.confidence, amount + other.amount)
    }
}

fun List<Fact>.sum(source: Subject, kind: String): Fact{
    return Fact(source, kind, sumOf { it.confidence }, sumOf { it.amount })
}

data class Relationship(val source: Subject, val kind: String, val relatesTo: Subject, val confidence: Int, val amount: Int = 0)

//Extract to somewhere else
private val bob = Subject(Thing("Bob"))
private val player = Subject(Thing("Player"))
private val loc = Subject(LocationNode("Kanbara Home"))

//In the player's mind
val houseOwnership = Relationship(bob, "Owns", loc, 100)
val location = Fact(loc, "Exists", 100, 100) //0 would mean confident it does NOT exist
val shopKeeperIsRich = Fact(Subject(bob.name, bob.thing, propertyTag = "Rich"), "Is", 50, 100)
val playerLikesBob = Relationship(player, "Likes", bob, 100, 25)

//In the NPC's mind
val npcBelievesPlayerLovesThem = Relationship(player, "Likes", bob, 50, 100)
