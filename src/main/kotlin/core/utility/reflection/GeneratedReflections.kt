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

private val commands: List<core.commands.Command> = listOf(system.help.CommandsCommand(), travel.jump.JumpCommand(), travel.move.MoveCommand(), system.RedoCommand(), combat.approach.RetreatCommand(), travel.RouteCommand(), system.debug.DebugCommand(), crafting.CraftRecipeCommand(), explore.LookCommand(), crafting.RecipeCommand(), system.persistance.saving.SaveCommand(), travel.climb.ClimbCommand(), travel.TravelCommand(), system.ExitCommand(), combat.block.BlockCommand(), status.journal.JournalCommand(), crafting.CookCommand(), system.history.HistoryCommand(), explore.map.ReadMapCommand(), travel.TravelInDirectionCommand(), core.commands.UnknownCommand(), interact.magic.CastCommand(), inventory.pickupItem.PickupItemCommand(), interact.UseCommand(), inventory.dropItem.PlaceItemCommand(), inventory.InventoryCommand(), interact.interaction.nothing.NothingCommand(), inventory.equipItem.EquipItemCommand(), status.rest.RestCommand(), combat.approach.ApproachCommand(), interact.eat.EatCommand(), combat.dodge.DodgeCommand(), inventory.EquippedCommand(), system.help.HelpCommand(), combat.attack.AttackCommand(), travel.climb.DismountCommand(), inventory.unEquipItem.UnEquipItemCommand(), status.StatusCommand())

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.water.Heal(), interact.magic.spellCommands.air.Adrenaline(), interact.magic.spellCommands.water.Jet(), interact.magic.spellCommands.air.Push(), interact.magic.spellCommands.air.Pull(), interact.magic.spellCommands.water.Poison(), interact.magic.spellCommands.earth.Rooted())

private val eventListeners: List<core.events.EventListener<*>> = listOf(status.journal.ViewQuestJournal(), system.message.DisplayMessage(), status.Status(), inventory.pickupItem.ItemPickedUp(), core.history.SessionListener(), status.statChanged.CreatureDied(), status.statChanged.StatChanged(), interact.actions.ScratchSurface(), interact.interaction.nothing.DoNothing(), core.gameState.quests.SetQuestStage(), status.effects.AddCondition(), combat.attack.StartAttack(), interact.actions.UseFoodItem(), travel.ViewRoute(), travel.move.Move(), status.statChanged.StatMinned(), interact.scope.spawn.SpawnItem(), status.rest.Rest(), crafting.Craft(), system.history.ViewChatHistory(), inventory.equipItem.ItemEquipped(), status.journal.ViewQuestList(), status.LevelUp(), interact.interaction.Interact(), interact.interaction.nothing.StartNothing(), interact.actions.UseOnFire(), interact.actions.OutOfReachUse(), interact.magic.StartCastSpell(), interact.scope.ArrivalHandler(), status.statChanged.PlayerStatMaxed(), core.gameState.SetProperties(), system.persistance.saving.Save(), interact.eat.EatFood(), interact.scope.spawn.ActivatorSpawner(), combat.block.StartBlock(), combat.approach.Move(), travel.Arrive(), status.ExpGained(), explore.map.ReadMap(), combat.dodge.Dodge(), combat.battle.CombatantDied(), combat.block.Block(), status.propValChanged.PropertyStatChanged(), interact.scope.remove.RemoveItem(), system.gameTick.TimeManager(), interact.scope.remove.RemoveScope(), system.TurnListener(), inventory.ListInventory(), system.gameTick.GameTick(), status.effects.ApplyEffects(), interact.magic.ViewWordHelp(), inventory.dropItem.TransferItem(), inventory.unEquipItem.ItemUnEquipped(), combat.takeDamage.TakeDamage(), system.debug.DebugListListener(), status.statChanged.StatBoosted(), interact.actions.StartFire(), core.gameState.quests.QuestListener(), travel.jump.PlayerFall(), system.help.ViewHelp(), combat.battle.BattleTurn(), status.effects.RemoveCondition(), interact.actions.UseItemOnIngredientRecipe(), interact.actions.NoUseFound(), travel.climb.AttemptClimb(), explore.Look(), travel.climb.ClimbComplete(), system.debug.DebugToggleListener(), inventory.equipItem.EquipItem(), interact.actions.ChopWood(), crafting.CheckRecipes(), interact.interaction.NoInteractionFound(), travel.jump.PlayerJump(), crafting.DiscoverRecipe(), combat.approach.StartMove(), inventory.unEquipItem.UnEquipItem(), inventory.dropItem.ItemDropped(), system.GameManager.MessageHandler(), travel.FindRoute(), status.statChanged.PlayerStatMinned(), system.item.ItemSpawner(), core.gameState.quests.CompleteQuest(), interact.magic.CastSpell(), combat.battle.BattleTick(), combat.dodge.StartDodge(), explore.RestrictLocation(), combat.attack.Attack(), interact.actions.UseIngredientOnActivatorRecipe(), interact.actions.DamageCreature(), system.debug.DebugStatListener(), travel.TravelStart(), system.debug.DebugTagListener())

