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

class Player : Target(name = "Player", dynamicDescription = DialogueOptions("Our Hero!"), body = "Human", location = LocationManager.findLocation("an open field")) {

    var climbJourney: ClimbJourney? = null
    var route: Route? = null

    var canRest = true
    var canTravel = true
    var canInteract = true
    val knownRecipes = NameSearchableList<Recipe>()

    init {
        soul.addStat(HEALTH, 1, 10, 1)
        soul.addStat(PERCEPTION, 1, 1, 1)
        soul.addStat(STAMINA, 1, 100, 1)
        soul.addStat(STRENGTH, 1, 1, 1)
        soul.addStat(WISDOM, 1, 1, 1)
        soul.addStat(CLIMBING, 1)
        soul.addStat(AGILITY, 1)
        soul.addStat(COOKING, 1)
        properties.tags.add("Open")
        properties.tags.add("Container")
        properties.tags.add("Creature")
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