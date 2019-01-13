package core.gameState

import core.gameState.location.LocationNode
import core.gameState.location.Route
import core.gameState.stat.Stat
import core.utility.NameSearchableList
import crafting.Recipe
import system.BodyManager
import system.location.LocationManager
import travel.climb.ClimbJourney

class Player : Target {
    val creature = Creature("Player", "Our Hero!", BodyManager.getBody("Human"), LocationManager.findLocation("an open field"), null, this)
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
        creature.soul.addStat(Stat.HEALTH, 1, 10, 1)
        creature.soul.addStat(Stat.STAMINA, 1, 100, 1)
        creature.soul.addStat(Stat.STRENGTH, 1, 1, 1)
        creature.soul.addStat(Stat.CLIMBING, 1)
        creature.soul.addStat(Stat.AGILITY, 1)
        creature.soul.addStat(Stat.COOKING, 1)
        creature.properties.tags.add("Container")
    }

    override fun toString(): String {
        return name
    }

    fun finishJourney(){
        climbJourney = null
        GameState.player.canRest = true
    }


}