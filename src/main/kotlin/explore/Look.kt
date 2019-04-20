package explore

import core.events.EventListener
import core.gameState.*
import core.gameState.Target
import core.history.display
import interact.scope.ScopeManager
import travel.climb.ClimbJourney

class Look : EventListener<LookEvent>() {


    override fun execute(event: LookEvent) {
        if (GameState.player.climbJourney != null && GameState.player.climbJourney is ClimbJourney) {
            ClimbLook.describeClimbJourney()
        } else if (event.target != null) {
            describeTarget(event.target)
        } else if (GameState.battle != null) {
            GameState.battle?.describe()
        } else {
            describeLocation()
        }
    }

    private fun describeTarget(target: Target) {
        var message = target.getDisplayName()
        message += "\n\t${target.description}"
        message += describeStatusEffects(target)
        message += describeWeight(target)
        message += describeProperties(target)
        display(message)
    }

    private fun describeStatusEffects(target: Target) : String {
        if (target.soul.effects.isNotEmpty()) {
            val effects = target.soul.effects.joinToString(", ") { it.name }
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

    private fun describeWeight(target: Target): String {
        return if (target.getWeight() > 0) {
            "\n\tWeight: ${target.getWeight()}"
        } else {
            ""
        }
    }

    private fun describeLocation() {
        display(GameState.player.location.getDescription())
        if (ScopeManager.getScope().getAllTargets().size > 1) {
            val targetList = targetsToString(ScopeManager.getScope().getAllTargets().filterNot { it == GameState.player })
            display("You find yourself surrounded by $targetList.")
        } else {
            display("You don't see anything of use.")
        }
    }


}