package core.gameState

import core.events.Event
import core.gameState.location.LocationNode
import core.gameState.location.Route
import core.gameState.stat.*
import core.utility.NameSearchableList
import crafting.Recipe
import dialogue.DialogueOptions
import system.location.LocationManager

const val PLAYER_START_NETWORK = "Kanbara Countryside"
const val PLAYER_START_LOCATION = "An Open Field"

class Player(
        name: String = "Player",
        dynamicDescription: DialogueOptions = DialogueOptions("Our Hero!"),
        body: String = "Human",
        location: LocationNode = LocationManager.getNetwork(PLAYER_START_NETWORK).findLocation(PLAYER_START_LOCATION))
    : Target(name = name, dynamicDescription = dynamicDescription, body = body, location = location) {

    var climbTarget: Target? = null
    var route: Route? = null

    var isClimbing = false
    var canRest = true
    var canTravel = true
    var canInteract = true
    val knownRecipes = NameSearchableList<Recipe>()

    init {
        soul.addStat(HEALTH, 1, 10, 1)
        soul.addStat(PERCEPTION, 1, 1, 1)
        soul.addStat(STAMINA, 1, 100, 1)
        soul.addStat(FOCUS, 1, 100, 1)
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

    fun setClimbing(target: Target) {
        isClimbing = true
        climbTarget = target
        canRest = false
        canTravel = false
        canInteract = false
    }

    fun finishClimbing() {
        isClimbing = false
        climbTarget = null
        canRest = true
        canTravel = true
        canInteract = true
    }


}