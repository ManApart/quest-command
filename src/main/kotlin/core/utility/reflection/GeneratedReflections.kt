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

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.air.Pull(), interact.magic.spellCommands.earth.Rooted(), interact.magic.spellCommands.air.Push(), interact.magic.spellCommands.water.Heal(), interact.magic.spellCommands.water.Jet(), interact.magic.spellCommands.water.Poison(), interact.magic.spellCommands.air.Adrenaline())

private val eventListeners: List<core.events.EventListener<*>> = listOf(inventory.equipItem.EquipItem(), status.journal.ViewQuestList(), combat.attack.StartAttack(), system.help.ViewHelp(), travel.TravelStart(), crafting.DiscoverRecipe(), system.debug.DebugTagListener(), system.TurnListener(), combat.takeDamage.TakeDamage(), interact.magic.CastSpell(), status.effects.ApplyEffects(), core.history.SessionListener(), status.statChanged.PlayerStatMaxed(), travel.FindRoute(), interact.interaction.Interact(), status.journal.ViewQuestJournal(), system.message.DisplayMessage(), travel.Arrive(), system.history.ViewChatHistory(), interact.scope.spawn.ActivatorSpawner(), core.gameState.quests.QuestListener(), interact.scope.spawn.SpawnItem(), combat.approach.StartMove(), inventory.dropItem.TransferItem(), travel.jump.PlayerFall(), travel.climb.ClimbComplete(), interact.actions.UseItemOnIngredientRecipe(), interact.actions.UseOnFire(), status.statChanged.StatBoosted(), inventory.dropItem.ItemDropped(), system.gameTick.TimeManager(), status.statChanged.StatMinned(), interact.eat.EatFood(), inventory.unEquipItem.ItemUnEquipped(), inventory.equipItem.ItemEquipped(), interact.interaction.NoInteractionFound(), interact.actions.UseFoodItem(), system.gameTick.GameTick(), combat.battle.BattleTick(), status.propValChanged.PropertyStatChanged(), system.debug.DebugToggleListener(), system.debug.DebugStatListener(), core.gameState.SetProperties(), status.statChanged.StatChanged(), interact.actions.ScratchSurface(), interact.actions.DamageCreature(), explore.Look(), system.debug.DebugListListener(), inventory.unEquipItem.UnEquipItem(), system.GameManager.MessageHandler(), combat.block.Block(), interact.interaction.nothing.DoNothing(), status.ExpGained(), crafting.Craft(), interact.actions.ChopWood(), combat.dodge.StartDodge(), combat.battle.CombatantDied(), combat.approach.Move(), interact.actions.UseIngredientOnActivatorRecipe(), inventory.pickupItem.ItemPickedUp(), combat.dodge.Dodge(), status.statChanged.CreatureDied(), status.rest.Rest(), interact.magic.StartCastSpell(), interact.actions.StartFire(), explore.RestrictLocation(), interact.actions.NoUseFound(), interact.scope.remove.RemoveScope(), status.effects.RemoveCondition(), status.effects.AddCondition(), travel.climb.AttemptClimb(), status.LevelUp(), status.statChanged.PlayerStatMinned(), system.persistance.saving.Save(), inventory.ListInventory(), interact.magic.ViewWordHelp(), explore.map.ReadMap(), status.Status(), interact.interaction.nothing.StartNothing(), travel.jump.PlayerJump(), combat.block.StartBlock(), system.item.ItemSpawner(), travel.ViewRoute(), combat.attack.Attack(), interact.scope.remove.RemoveItem(), core.gameState.quests.CompleteQuest(), interact.scope.ArrivalHandler(), crafting.CheckRecipes(), combat.battle.BattleTurn(), core.gameState.quests.SetQuestStage())

