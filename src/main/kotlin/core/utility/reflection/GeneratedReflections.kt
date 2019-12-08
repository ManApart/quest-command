package core.utility.reflection

import core.commands.Command
import core.events.Event
import core.events.EventListener
import core.gameState.dataParsing.events.EventParser
import interact.magic.spellCommands.SpellCommand

class GeneratedReflections : Reflections {
    override fun getCommands(): List<Command> {
        return commands
    }

    override fun getSpellCommands(): List<SpellCommand> {
        return spellCommands
    }
    
    override fun getEventParsers(): List<EventParser> {
        return eventParsers
    }

    override fun getEventListeners(): List<EventListener<*>> {
        return eventListeners
    }
}

private val commands: List<core.commands.Command> = listOf(system.help.CommandsCommand(), travel.jump.JumpCommand(), travel.move.MoveCommand(), system.RedoCommand(), combat.approach.RetreatCommand(), travel.RouteCommand(), system.debug.DebugCommand(), crafting.CraftRecipeCommand(), explore.LookCommand(), crafting.RecipeCommand(), system.persistance.saving.SaveCommand(), travel.climb.ClimbCommand(), travel.TravelCommand(), system.ExitCommand(), combat.block.BlockCommand(), status.journal.JournalCommand(), crafting.CookCommand(), system.history.HistoryCommand(), explore.map.ReadMapCommand(), travel.TravelInDirectionCommand(), core.commands.UnknownCommand(), interact.magic.CastCommand(), inventory.pickupItem.PickupItemCommand(), interact.UseCommand(), inventory.dropItem.PlaceItemCommand(), inventory.InventoryCommand(), interact.interaction.nothing.NothingCommand(), inventory.equipItem.EquipItemCommand(), status.rest.RestCommand(), combat.approach.ApproachCommand(), interact.eat.EatCommand(), combat.dodge.DodgeCommand(), inventory.EquippedCommand(), system.help.HelpCommand(), combat.attack.AttackCommand(), travel.climb.DismountCommand(), inventory.unEquipItem.UnEquipItemCommand(), status.StatusCommand())

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.earth.Rock(), interact.magic.spellCommands.fire.Flame(), interact.magic.spellCommands.water.Jet(), interact.magic.spellCommands.air.Push(), interact.magic.spellCommands.air.Adrenaline(), interact.magic.spellCommands.earth.Rooted(), interact.magic.spellCommands.water.Heal(), interact.magic.spellCommands.air.Pull(), interact.magic.spellCommands.water.Poison())

private val eventParsers: List<core.gameState.dataParsing.events.EventParser> = listOf(core.gameState.dataParsing.events.RemoveConditionEventParser(), core.gameState.dataParsing.events.ArriveEventParser(), core.gameState.dataParsing.events.SpawnActivatorEventParser(), core.gameState.dataParsing.events.RemoveItemEventParser(), core.gameState.dataParsing.events.StatChangeEventParser(), core.gameState.dataParsing.events.MessageEventParser(), core.gameState.dataParsing.events.CompleteQuestEventParser(), core.gameState.dataParsing.events.RemoveScopeEventParser(), core.gameState.dataParsing.events.RestrictLocationEventParser(), core.gameState.dataParsing.events.SpawnItemEventParser(), core.gameState.dataParsing.events.SetQuestStageEventParser(), core.gameState.dataParsing.events.DiscoverRecipeEventParser(), core.gameState.dataParsing.events.SetPropertiesEventParser(), core.gameState.dataParsing.events.AddConditionEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(inventory.dropItem.TransferItem(), status.journal.ViewQuestJournal(), system.message.DisplayMessage(), interact.magic.ViewWordHelp(), status.statChanged.StatChanged(), system.history.ViewChatHistory(), core.gameState.quests.SetQuestStage(), system.debug.DebugStatListener(), core.gameState.quests.QuestListener(), inventory.unEquipItem.ItemUnEquipped(), interact.actions.NoUseFound(), system.gameTick.TimeManager(), system.debug.DebugTagListener(), explore.Look(), interact.actions.UseFoodItem(), status.statChanged.StatBoosted(), status.journal.ViewQuestList(), status.Status(), status.LevelUp(), interact.interaction.nothing.DoNothing(), interact.interaction.nothing.StartNothing(), status.statChanged.StatMinned(), status.statChanged.PlayerStatMaxed(), interact.scope.remove.RemoveItem(), status.statChanged.CreatureDied(), inventory.equipItem.EquipItem(), interact.eat.EatFood(), system.gameTick.GameTick(), status.ExpGained(), system.GameManager.MessageHandler(), combat.dodge.StartDodge(), interact.interaction.NoInteractionFound(), core.history.SessionListener(), system.help.ViewHelp(), inventory.dropItem.ItemDropped(), interact.magic.CastSpell(), status.statChanged.PlayerStatMinned(), crafting.DiscoverRecipe(), inventory.unEquipItem.UnEquipItem(), interact.actions.UseItemOnIngredientRecipe(), travel.ViewRoute(), combat.block.StartBlock(), travel.jump.PlayerFall(), crafting.Craft(), travel.move.Move(), interact.actions.ScratchSurface(), system.TurnListener(), combat.battle.BattleTurn(), core.gameState.quests.CompleteQuest(), system.debug.DebugToggleListener(), status.rest.Rest(), system.debug.DebugListListener(), combat.dodge.Dodge(), combat.takeDamage.TakeDamage(), interact.interaction.Interact(), interact.actions.ChopWood(), inventory.pickupItem.ItemPickedUp(), inventory.ListInventory(), interact.actions.StartFire(), travel.climb.ClimbComplete(), travel.climb.AttemptClimb(), system.item.ItemSpawner(), interact.scope.remove.RemoveScope(), crafting.CheckRecipes(), status.propValChanged.PropertyStatChanged(), system.persistance.saving.Save(), interact.actions.UseOnFire(), combat.battle.BattleTick(), travel.FindRoute(), combat.block.Block(), core.gameState.SetProperties(), interact.magic.StartCastSpell(), interact.scope.ArrivalHandler(), interact.scope.spawn.ActivatorSpawner(), explore.RestrictLocation(), explore.map.ReadMap(), status.effects.RemoveCondition(), status.propValChanged.PropertyStatMinned(), interact.actions.UseIngredientOnActivatorRecipe(), interact.scope.spawn.SpawnItem(), inventory.equipItem.ItemEquipped(), status.effects.ApplyEffects(), combat.attack.Attack(), combat.approach.Move(), travel.jump.PlayerJump(), combat.attack.StartAttack(), travel.TravelStart(), status.effects.AddCondition(), travel.Arrive(), interact.actions.DamageCreature(), combat.battle.CombatantDied(), interact.actions.OutOfReachUse(), combat.approach.StartMove())

