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

private val commands: List<core.commands.Command> = listOf(inventory.dropItem.PlaceItemCommand(), crafting.CookCommand(), system.persistance.saving.SaveCommand(), explore.map.ReadMapCommand(), inventory.InventoryCommand(), travel.climb.DismountCommand(), combat.attack.AttackCommand(), travel.climb.ClimbCommand(), status.rest.RestCommand(), crafting.CraftRecipeCommand(), core.commands.UnknownCommand(), system.help.CommandsCommand(), combat.block.BlockCommand(), status.StatusCommand(), system.debug.DebugCommand(), combat.approach.RetreatCommand(), combat.approach.ApproachCommand(), explore.LookCommand(), inventory.unEquipItem.UnEquipItemCommand(), interact.interaction.nothing.NothingCommand(), combat.dodge.DodgeCommand(), travel.TravelInDirectionCommand(), inventory.pickupItem.PickupItemCommand(), interact.eat.EatCommand(), system.RedoCommand(), travel.jump.JumpCommand(), travel.TravelCommand(), inventory.equipItem.EquipItemCommand(), inventory.EquippedCommand(), interact.magic.CastCommand(), crafting.RecipeCommand(), system.help.HelpCommand(), interact.UseCommand(), status.journal.JournalCommand(), system.ExitCommand(), travel.RouteCommand(), system.history.HistoryCommand())

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.air.Pull(), interact.magic.spellCommands.air.Push(), interact.magic.spellCommands.water.Heal(), interact.magic.spellCommands.water.Jet())

private val eventListeners: List<core.events.EventListener<*>> = listOf(system.TurnListener(), status.propValChanged.PropertyStatChanged(), combat.battle.BattleTurn(), combat.block.StartBlock(), system.help.ViewHelp(), inventory.dropItem.TransferItem(), core.gameState.quests.SetQuestStage(), status.statChanged.StatBoosted(), interact.actions.UseFoodItem(), explore.Look(), inventory.pickupItem.ItemPickedUp(), interact.scope.remove.RemoveItem(), core.history.SessionListener(), combat.approach.StartMove(), interact.actions.ScratchSurface(), combat.battle.CombatantDied(), interact.magic.StartCastSpell(), status.journal.ViewQuestJournal(), system.debug.DebugStatListener(), core.gameState.quests.QuestListener(), crafting.DiscoverRecipe(), system.persistance.saving.Save(), interact.actions.NoUseFound(), interact.scope.spawn.ActivatorSpawner(), status.statChanged.PlayerStatMaxed(), status.statChanged.PlayerStatMinned(), inventory.equipItem.ItemEquipped(), combat.dodge.Dodge(), core.gameState.SetProperties(), system.gameTick.GameTick(), interact.scope.remove.RemoveScope(), inventory.equipItem.EquipItem(), status.ExpGained(), status.statChanged.StatMinned(), interact.actions.UseOnFire(), combat.attack.Attack(), system.debug.DebugListListener(), status.LevelUp(), status.effects.AddCondition(), travel.Arrive(), status.effects.ApplyEffects(), explore.map.ReadMap(), interact.interaction.Interact(), interact.scope.ArrivalHandler(), interact.actions.ChopWood(), travel.ViewRoute(), status.statChanged.CreatureDied(), crafting.Craft(), interact.actions.StartFire(), status.statChanged.StatChanged(), status.rest.Rest(), travel.climb.ClimbComplete(), explore.RestrictLocation(), system.history.ViewChatHistory(), combat.battle.BattleTick(), status.Status(), crafting.CheckRecipes(), interact.eat.EatFood(), inventory.unEquipItem.ItemUnEquipped(), core.gameState.quests.CompleteQuest(), travel.jump.PlayerJump(), interact.interaction.nothing.StartNothing(), travel.climb.AttemptClimb(), combat.approach.Move(), travel.FindRoute(), inventory.dropItem.ItemDropped(), interact.interaction.NoInteractionFound(), interact.actions.DamageCreature(), interact.actions.UseItemOnIngredientRecipe(), system.item.ItemSpawner(), inventory.ListInventory(), travel.jump.PlayerFall(), status.journal.ViewQuestList(), combat.block.Block(), combat.dodge.StartDodge(), system.gameTick.TimeManager(), system.debug.DebugTagListener(), travel.TravelStart(), system.debug.DebugToggleListener(), system.message.DisplayMessage(), combat.takeDamage.TakeDamage(), interact.scope.spawn.SpawnItem(), combat.attack.StartAttack(), interact.magic.ViewWordHelp(), system.GameManager.MessageHandler(), interact.interaction.nothing.DoNothing(), status.effects.RemoveCondition(), inventory.unEquipItem.UnEquipItem(), interact.actions.UseIngredientOnActivatorRecipe(), interact.magic.CastSpell())

