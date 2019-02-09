package core.gameState

import core.events.Event
import core.gameState.location.LocationNode
import core.gameState.location.Route
import core.gameState.stat.*
import core.utility.NameSearchableList
import crafting.Recipe
import dialogue.DialogueOptions
import system.body.BodyManager
import system.location.LocationManager
import travel.climb.ClimbJourney

class Player(creatureBase: Creature? = null) : Target {
    val creature = creatureBase ?: Creature("Player", DialogueOptions("Our Hero!"), BodyManager.getBody("Human"), LocationManager.findLocation("an open field"), null, this)
    var climbJourney: ClimbJourney? = null
    var route: Route? = null

    override val name: String get() = creature.name
    override val description: String get() = creature.description
    override val inventory: Inventory get() = creature.inventory
    override val location: LocationNode get() = creature.location
    override val properties: Properties get() = creature.properties
    var canRest = true
    var canTravel = true
    var canInteract = true
    val knownRecipes = NameSearchableList<Recipe>()

    init {
        creature.soul.addStat(HEALTH, 1, 10, 1)
        creature.soul.addStat(PERCEPTION, 1, 1, 1)
        creature.soul.addStat(STAMINA, 1, 100, 1)
        creature.soul.addStat(STRENGTH, 1, 1, 1)
        creature.soul.addStat(WISDOM, 1, 1, 1)
        creature.soul.addStat(CLIMBING, 1)
        creature.soul.addStat(AGILITY, 1)
        creature.soul.addStat(COOKING, 1)
        creature.properties.tags.add("Open")
        creature.properties.tags.add("Container")
    }

    override fun toString(): String {
        return name
    }

    override fun canConsume(event: Event): Boolean {
        return false
    }

    override fun consume(event: Event) {}

    fun finishJourney() {
        climbJourney = null
        GameState.player.canRest = true
    }


}