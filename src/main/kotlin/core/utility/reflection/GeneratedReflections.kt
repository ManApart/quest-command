package core.utility.reflection

import core.commands.Command
import core.events.EventListener
import interact.magic.spellCommands.SpellCommand

class GeneratedReflections : Reflections {
    override fun getCommands(): List<Command> {
        return commands
    }

    override fun getSpellCommands(): List<SpellCommand> {
        return spellCommands
    }

    override fun getEventListeners(): List<EventListener<*>> {
        return eventListeners
    }
}

private val commands: List<Command> = listOf(combat.approach.AproachCommand(), combat.attack.AttackCommand(), interact.magic.CastCommand(), explore.LookCommand(), core.commands.UnknownCommand(), system.RedoCommand(), inventory.unEquipItem.UnEquipItemCommand(), inventory.InventoryCommand(), status.rest.RestCommand(), crafting.CookCommand(), crafting.RecipeCommand(), crafting.CraftRecipeCommand(), status.StatusCommand(), inventory.EquippedCommand(), inventory.pickupItem.PickupItemCommand(), system.ExitCommand(), travel.jump.JumpCommand(), travel.climb.DismountCommand(), inventory.dropItem.PlaceItemCommand(), status.journal.JournalCommand(), combat.approach.RetreatCommand(), inventory.equipItem.EquipItemCommand(), travel.climb.ClimbCommand(), system.history.HistoryCommand(), explore.map.ReadMapCommand(), system.help.HelpCommand(), combat.block.BlockCommand(), interact.UseCommand(), interact.eat.EatCommand(), system.help.CommandsCommand(), combat.dodge.DodgeCommand(), travel.TravelInDirectionCommand(), travel.TravelCommand(), travel.RouteCommand())

private val spellCommands: List<SpellCommand> = listOf(interact.magic.spellCommands.water.Jet())

private val eventListeners: List<EventListener<*>> = listOf(system.item.ItemSpawner(), inventory.pickupItem.ItemPickedUp(), combat.battle.BattleTick(), core.gameState.quests.SetQuestStage(), inventory.equipItem.ItemEquipped(), combat.battle.BattleTurn(), interact.actions.UseOnFire(), interact.scope.remove.RemoveScope(), status.Status(), status.journal.ViewQuestList(), status.statChanged.StatBoosted(), system.history.ViewChatHistory(), interact.interaction.Interact(), interact.actions.DamageCreature(), interact.actions.StartFire(), travel.TravelStart(), interact.actions.UseIngredientOnActivatorRecipe(), inventory.unEquipItem.UnEquipItem(), combat.takeDamage.TakeDamage(), travel.FindRoute(), explore.Look(), interact.actions.ScratchSurface(), crafting.CheckRecipes(), system.help.ViewHelp(), travel.climb.ClimbComplete(), status.statChanged.StatChanged(), interact.scope.spawn.ActivatorSpawner(), travel.jump.PlayerFall(), core.gameState.quests.CompleteQuest(), combat.block.StartBlock(), interact.scope.spawn.SpawnItem(), status.effects.AddCondition(), inventory.dropItem.ItemDropped(), crafting.DiscoverRecipe(), system.gameTick.TimeManager(), status.statChanged.CreatureDied(), inventory.unEquipItem.ItemUnEquipped(), status.statChanged.PlayerStatMinned(), interact.scope.ArrivalHandler(), status.LevelUp(), combat.attack.Attack(), travel.jump.PlayerJump(), interact.actions.UseItemOnIngredientRecipe(), interact.actions.StatMinned(), system.GameManager.MessageHandler(), inventory.equipItem.EquipItem(), combat.approach.StartApproach(), status.ExpGained(), status.effects.ApplyEffects(), combat.block.Block(), inventory.ListInventory(), core.gameState.SetProperties(), combat.approach.Approach(), interact.magic.ViewWordHelp(), explore.map.ReadMap(), travel.ViewRoute(), status.statChanged.PlayerStatMaxed(), status.rest.Rest(), combat.battle.CombatantDied(), interact.actions.UseFoodItem(), interact.interaction.NoInteractionFound(), interact.eat.EatFood(), core.gameState.quests.QuestListener(), interact.actions.ChopWood(), crafting.Craft(), inventory.dropItem.TransferItem(), status.journal.ViewQuestJournal(), status.effects.RemoveCondition(), combat.dodge.Dodge(), explore.RestrictLocation(), combat.attack.StartAttack(), travel.Arrive(), interact.actions.NoUseFound(), combat.dodge.StartDodge(), system.gameTick.GameTick(), interact.scope.remove.RemoveItem(), travel.climb.AttemptClimb())
