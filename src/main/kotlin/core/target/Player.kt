package core.target

import core.PLAYER_START_LOCATION
import core.PLAYER_START_NETWORK
import core.events.Event
import core.utility.NameSearchableList
import crafting.Recipe
import dialogue.DialogueOptions
import traveling.location.LocationManager
import traveling.location.LocationNode
import traveling.location.Route

class Player(
        name: String = "Player",
        dynamicDescription: DialogueOptions = DialogueOptions("Our Hero!"),
        body: String = "Human",
        location: LocationNode = LocationManager.getNetwork(PLAYER_START_NETWORK).findLocation(PLAYER_START_LOCATION))
    : Target(name = name, dynamicDescription = dynamicDescription, aiName= core.ai.PLAYER_CONTROLLED_ID, body = body, location = location) {

    var climbTarget: Target? = null
    var route: Route? = null

    var isClimbing = false
    var canRest = true
    var canTravel = true
    var canInteract = true
    val knownRecipes = NameSearchableList<Recipe>()


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