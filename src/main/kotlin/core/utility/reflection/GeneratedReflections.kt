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

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.air.Pull(), interact.magic.spellCommands.air.Push(), interact.magic.spellCommands.water.Heal(), interact.magic.spellCommands.water.Jet(), interact.magic.spellCommands.water.Poison(), interact.magic.spellCommands.air.Adrenaline())

private val eventListeners: List<core.events.EventListener<*>> = listOf(interact.scope.remove.RemoveScope(), core.gameState.quests.QuestListener(), core.history.SessionListener(), interact.actions.UseIngredientOnActivatorRecipe(), combat.battle.BattleTurn(), status.statChanged.PlayerStatMinned(), interact.interaction.Interact(), interact.actions.DamageCreature(), status.ExpGained(), system.persistance.saving.Save(), inventory.unEquipItem.UnEquipItem(), travel.TravelStart(), combat.attack.Attack(), status.Status(), system.debug.DebugListListener(), inventory.equipItem.EquipItem(), status.journal.ViewQuestList(), interact.interaction.NoInteractionFound(), interact.eat.EatFood(), combat.battle.CombatantDied(), system.message.DisplayMessage(), interact.actions.UseItemOnIngredientRecipe(), crafting.Craft(), combat.block.Block(), combat.approach.Move(), interact.actions.UseFoodItem(), status.statChanged.CreatureDied(), travel.jump.PlayerFall(), interact.actions.ChopWood(), interact.scope.remove.RemoveItem(), explore.map.ReadMap(), status.journal.ViewQuestJournal(), status.statChanged.StatMinned(), interact.interaction.nothing.DoNothing(), interact.scope.spawn.ActivatorSpawner(), travel.climb.AttemptClimb(), explore.RestrictLocation(), system.debug.DebugTagListener(), system.item.ItemSpawner(), system.debug.DebugStatListener(), interact.actions.NoUseFound(), crafting.CheckRecipes(), system.GameManager.MessageHandler(), system.gameTick.GameTick(), system.TurnListener(), interact.scope.ArrivalHandler(), interact.actions.StartFire(), core.gameState.quests.SetQuestStage(), inventory.ListInventory(), system.gameTick.TimeManager(), system.debug.DebugToggleListener(), combat.takeDamage.TakeDamage(), travel.climb.ClimbComplete(), interact.magic.CastSpell(), status.effects.RemoveCondition(), travel.ViewRoute(), status.propValChanged.PropertyStatChanged(), inventory.equipItem.ItemEquipped(), core.gameState.quests.CompleteQuest(), system.help.ViewHelp(), combat.battle.BattleTick(), crafting.DiscoverRecipe(), travel.Arrive(), interact.actions.UseOnFire(), combat.dodge.StartDodge(), status.statChanged.PlayerStatMaxed(), status.rest.Rest(), interact.actions.ScratchSurface(), combat.dodge.Dodge(), status.effects.ApplyEffects(), interact.magic.StartCastSpell(), interact.scope.spawn.SpawnItem(), interact.interaction.nothing.StartNothing(), system.history.ViewChatHistory(), explore.Look(), core.gameState.SetProperties(), status.statChanged.StatChanged(), combat.attack.StartAttack(), inventory.dropItem.TransferItem(), status.LevelUp(), inventory.unEquipItem.ItemUnEquipped(), travel.jump.PlayerJump(), travel.FindRoute(), inventory.dropItem.ItemDropped(), interact.magic.ViewWordHelp(), combat.block.StartBlock(), status.statChanged.StatBoosted(), status.effects.AddCondition(), inventory.pickupItem.ItemPickedUp(), combat.approach.StartMove())

