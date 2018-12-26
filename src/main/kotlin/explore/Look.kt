package explore

import core.events.EventListener
import core.gameState.*
import core.gameState.Target
import core.history.display
import explore.ClimbLook.describeClimbJourney
import interact.scope.ScopeManager
import travel.climb.ClimbJourney

class Look : EventListener<LookEvent>() {


    override fun execute(event: LookEvent) {
        if (GameState.player.climbJourney != null && GameState.player.climbJourney is ClimbJourney) {
            ClimbLook.describeClimbJourney()
        } else if (event.target != null) {
            describeTarget(event.target)
        } else {
            describeLocation()
        }
    }

    private fun describeTarget(target: Target) {
        var message = target.getDisplayName()
        message += "\n\t${target.description}"
        message += describeStatusEffects(target)
        message += describeProperties(target)
        display(message)
    }

    private fun describeStatusEffects(target: Target) : String {
        if (target.hasSoul() && target.getSoul()!!.effects.isNotEmpty()) {
            val effects = target.getSoul()!!.effects.joinToString(", ") { it.name }
            return "\n\t${target.name} is $effects"
        }
        return ""
    }

    private fun describeProperties(target: Target) : String {
        if (!target.properties.isEmpty()) {
            return "\n\t${target.properties}"
        }
        return ""
    }

    private fun describeLocation() {
        display(GameState.player.creature.location.getDescription())
        if (ScopeManager.getScope().getTargets().size > 1) {
            val targetList = targetsToString(ScopeManager.getScope().getTargets().filterNot { it == GameState.player })
            display("You find yourself surrounded by $targetList.")
        } else {
            display("You don't see anything of use.")
        }
    }


}