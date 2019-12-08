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

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.earth.Rock(), interact.magic.spellCommands.water.Poison(), interact.magic.spellCommands.water.Jet(), interact.magic.spellCommands.air.Push(), interact.magic.spellCommands.earth.Rooted(), interact.magic.spellCommands.water.Heal(), interact.magic.spellCommands.air.Pull(), interact.magic.spellCommands.air.Adrenaline())

private val eventParsers: List<core.gameState.dataParsing.events.EventParser> = listOf(core.gameState.dataParsing.events.RemoveItemEventParser(), core.gameState.dataParsing.events.MessageEventParser(), core.gameState.dataParsing.events.CompleteQuestEventParser(), core.gameState.dataParsing.events.ArriveEventParser(), core.gameState.dataParsing.events.RemoveConditionEventParser(), core.gameState.dataParsing.events.SpawnActivatorEventParser(), core.gameState.dataParsing.events.AddConditionEventParser(), core.gameState.dataParsing.events.SetPropertiesEventParser(), core.gameState.dataParsing.events.DiscoverRecipeEventParser(), core.gameState.dataParsing.events.StatChangeEventParser(), core.gameState.dataParsing.events.RestrictLocationEventParser(), core.gameState.dataParsing.events.RemoveScopeEventParser(), core.gameState.dataParsing.events.SpawnItemEventParser(), core.gameState.dataParsing.events.SetQuestStageEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(combat.block.Block(), inventory.equipItem.EquipItem(), status.journal.ViewQuestList(), system.GameManager.MessageHandler(), system.gameTick.GameTick(), crafting.Craft(), crafting.CheckRecipes(), combat.battle.CombatantDied(), combat.takeDamage.TakeDamage(), status.rest.Rest(), status.statChanged.StatChanged(), system.debug.DebugListListener(), interact.actions.StartFire(), status.statChanged.PlayerStatMaxed(), interact.actions.ChopWood(), core.gameState.quests.QuestListener(), combat.attack.StartAttack(), interact.scope.spawn.SpawnItem(), interact.actions.OutOfReachUse(), inventory.dropItem.TransferItem(), status.journal.ViewQuestJournal(), inventory.dropItem.ItemDropped(), core.gameState.SetProperties(), crafting.DiscoverRecipe(), interact.scope.remove.RemoveScope(), status.statChanged.StatMinned(), interact.interaction.Interact(), interact.actions.UseFoodItem(), explore.map.ReadMap(), travel.ViewRoute(), travel.climb.AttemptClimb(), travel.TravelStart(), core.gameState.quests.CompleteQuest(), interact.magic.ViewWordHelp(), system.history.ViewChatHistory(), interact.interaction.nothing.StartNothing(), status.statChanged.PlayerStatMinned(), inventory.ListInventory(), status.statChanged.CreatureDied(), travel.FindRoute(), interact.interaction.nothing.DoNothing(), combat.approach.Move(), system.persistance.saving.Save(), system.debug.DebugTagListener(), system.TurnListener(), interact.actions.DamageCreature(), system.help.ViewHelp(), inventory.unEquipItem.ItemUnEquipped(), interact.actions.NoUseFound(), travel.Arrive(), combat.approach.StartMove(), combat.battle.BattleTick(), status.ExpGained(), core.gameState.quests.SetQuestStage(), status.effects.RemoveCondition(), interact.actions.UseIngredientOnActivatorRecipe(), explore.Look(), combat.dodge.StartDodge(), explore.RestrictLocation(), interact.magic.StartCastSpell(), travel.jump.PlayerJump(), interact.actions.ScratchSurface(), interact.actions.UseItemOnIngredientRecipe(), status.effects.AddCondition(), system.debug.DebugStatListener(), travel.jump.PlayerFall(), system.item.ItemSpawner(), status.Status(), system.debug.DebugToggleListener(), system.gameTick.TimeManager(), interact.actions.UseOnFire(), status.statChanged.StatBoosted(), interact.eat.EatFood(), combat.attack.Attack(), travel.move.Move(), combat.dodge.Dodge(), inventory.pickupItem.ItemPickedUp(), combat.block.StartBlock(), system.message.DisplayMessage(), interact.scope.spawn.ActivatorSpawner(), inventory.unEquipItem.UnEquipItem(), interact.scope.remove.RemoveItem(), inventory.equipItem.ItemEquipped(), status.LevelUp(), core.history.SessionListener(), combat.battle.BattleTurn(), status.effects.ApplyEffects(), interact.interaction.NoInteractionFound(), interact.scope.ArrivalHandler(), status.propValChanged.PropertyStatChanged(), travel.climb.ClimbComplete(), interact.magic.CastSpell())

