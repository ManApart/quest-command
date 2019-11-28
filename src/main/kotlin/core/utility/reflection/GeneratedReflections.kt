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

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.air.Pull(), interact.magic.spellCommands.air.Push(), interact.magic.spellCommands.water.Heal(), interact.magic.spellCommands.water.Jet(), interact.magic.spellCommands.water.Poison())

private val eventListeners: List<core.events.EventListener<*>> = listOf(interact.actions.ScratchSurface(), combat.battle.CombatantDied(), travel.TravelStart(), core.gameState.quests.CompleteQuest(), inventory.pickupItem.ItemPickedUp(), interact.interaction.nothing.StartNothing(), system.debug.DebugListListener(), interact.scope.ArrivalHandler(), travel.climb.ClimbComplete(), system.history.ViewChatHistory(), inventory.ListInventory(), combat.battle.BattleTurn(), inventory.dropItem.ItemDropped(), combat.attack.StartAttack(), core.gameState.quests.SetQuestStage(), interact.scope.remove.RemoveScope(), core.gameState.quests.QuestListener(), travel.climb.AttemptClimb(), status.statChanged.StatMinned(), status.propValChanged.PropertyStatChanged(), status.journal.ViewQuestList(), status.statChanged.CreatureDied(), interact.magic.CastSpell(), system.debug.DebugToggleListener(), inventory.equipItem.ItemEquipped(), explore.RestrictLocation(), crafting.DiscoverRecipe(), combat.approach.Move(), status.effects.RemoveCondition(), interact.magic.ViewWordHelp(), status.statChanged.StatChanged(), inventory.equipItem.EquipItem(), status.journal.ViewQuestJournal(), combat.takeDamage.TakeDamage(), interact.eat.EatFood(), interact.magic.StartCastSpell(), status.statChanged.PlayerStatMaxed(), interact.interaction.Interact(), travel.jump.PlayerJump(), crafting.CheckRecipes(), status.rest.Rest(), status.effects.AddCondition(), system.gameTick.TimeManager(), system.debug.DebugTagListener(), interact.actions.DamageCreature(), status.statChanged.StatBoosted(), combat.dodge.StartDodge(), interact.actions.UseItemOnIngredientRecipe(), combat.approach.StartMove(), explore.Look(), explore.map.ReadMap(), interact.actions.NoUseFound(), status.ExpGained(), interact.actions.UseFoodItem(), system.persistance.saving.Save(), combat.dodge.Dodge(), travel.FindRoute(), system.debug.DebugStatListener(), interact.scope.spawn.ActivatorSpawner(), combat.block.StartBlock(), interact.actions.UseIngredientOnActivatorRecipe(), system.item.ItemSpawner(), status.statChanged.PlayerStatMinned(), interact.interaction.NoInteractionFound(), travel.jump.PlayerFall(), travel.ViewRoute(), combat.attack.Attack(), travel.Arrive(), system.TurnListener(), combat.battle.BattleTick(), inventory.unEquipItem.UnEquipItem(), interact.actions.UseOnFire(), system.message.DisplayMessage(), inventory.dropItem.TransferItem(), interact.actions.StartFire(), system.GameManager.MessageHandler(), system.gameTick.GameTick(), core.history.SessionListener(), combat.block.Block(), interact.scope.spawn.SpawnItem(), interact.interaction.nothing.DoNothing(), status.LevelUp(), status.Status(), interact.scope.remove.RemoveItem(), core.gameState.SetProperties(), inventory.unEquipItem.ItemUnEquipped(), interact.actions.ChopWood(), status.effects.ApplyEffects(), system.help.ViewHelp(), crafting.Craft())

