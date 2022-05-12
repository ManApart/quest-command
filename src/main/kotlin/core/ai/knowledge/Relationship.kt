package core.ai.knowledge

import core.thing.Thing
import traveling.location.network.LocationNode

enum class Confidence(val amount: Int){ UNKNOWN(0), GUESS(25), ESTIMATE(50), CONFIDENT(75), POSITIVE(100)}

fun Int.confidence(): Confidence {
    return Confidence.values().filter { it.amount < this }.maxOrNull()!!
}

data class Relationship(val source: Subject, val kind: String, val relatesTo: Subject, val confidence: Int, val amount: Int = 0)
data class Fact(val source: Subject, val kind: String, val confidence: Int, val amount: Int = 0)

private val bob = Subject(Thing("Bob"))
private val player = Subject(Thing("Player"))
private val loc = Subject(LocationNode("Kanbara Home"))

//In the player's mind
val houseOwnership = Relationship(bob, "Owns", loc, 100)
val location = Fact(loc, "Exists", 100)
val shopKeeperIsRich = Fact(Subject(bob.name, bob.thing, propertyTag = "Rich"), "Is", 50, 100)
val playerLikesBob = Relationship(player, "Likes", bob, 100, 25)

//In the NPC's mind
val npcBelievesPlayerLovesThem = Relationship(player, "Likes", bob, 50, 100)
