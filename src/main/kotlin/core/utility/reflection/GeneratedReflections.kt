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

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.water.Heal(), interact.magic.spellCommands.air.Adrenaline(), interact.magic.spellCommands.water.Jet(), interact.magic.spellCommands.air.Push(), interact.magic.spellCommands.air.Pull(), interact.magic.spellCommands.water.Poison(), interact.magic.spellCommands.earth.Rooted())

private val eventParsers: List<core.gameState.dataParsing.events.EventParser> = listOf(core.gameState.dataParsing.events.ArriveEventParser(), core.gameState.dataParsing.events.AddConditionEventParser(), core.gameState.dataParsing.events.MessageEventParser(), core.gameState.dataParsing.events.RemoveItemEventParser(), core.gameState.dataParsing.events.CompleteQuestEventParser(), core.gameState.dataParsing.events.SetQuestStageEventParser(), core.gameState.dataParsing.events.SpawnItemEventParser(), core.gameState.dataParsing.events.RemoveConditionEventParser(), core.gameState.dataParsing.events.RemoveScopeEventParser(), core.gameState.dataParsing.events.SpawnActivatorEventParser(), core.gameState.dataParsing.events.DiscoverRecipeEventParser(), core.gameState.dataParsing.events.SetPropertiesEventParser(), core.gameState.dataParsing.events.StatChangeEventParser(), core.gameState.dataParsing.events.RestrictLocationEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(system.debug.DebugToggleListener(), interact.scope.remove.RemoveScope(), core.gameState.quests.QuestListener(), system.gameTick.TimeManager(), system.debug.DebugTagListener(), interact.magic.CastSpell(), status.effects.AddCondition(), status.propValChanged.PropertyStatChanged(), interact.actions.OutOfReachUse(), travel.Arrive(), travel.move.Move(), core.gameState.quests.SetQuestStage(), combat.dodge.StartDodge(), combat.attack.Attack(), status.effects.RemoveCondition(), combat.battle.CombatantDied(), core.history.SessionListener(), system.message.DisplayMessage(), status.ExpGained(), combat.block.Block(), inventory.equipItem.EquipItem(), interact.scope.remove.RemoveItem(), system.GameManager.MessageHandler(), status.statChanged.PlayerStatMinned(), interact.actions.ScratchSurface(), status.journal.ViewQuestJournal(), system.debug.DebugListListener(), explore.RestrictLocation(), interact.actions.UseItemOnIngredientRecipe(), status.statChanged.StatChanged(), travel.FindRoute(), interact.magic.StartCastSpell(), combat.battle.BattleTurn(), combat.block.StartBlock(), core.gameState.SetProperties(), inventory.dropItem.TransferItem(), interact.interaction.nothing.StartNothing(), explore.Look(), crafting.DiscoverRecipe(), status.Status(), combat.takeDamage.TakeDamage(), inventory.equipItem.ItemEquipped(), system.history.ViewChatHistory(), interact.interaction.Interact(), interact.actions.DamageCreature(), interact.scope.ArrivalHandler(), interact.actions.UseIngredientOnActivatorRecipe(), interact.interaction.nothing.DoNothing(), status.rest.Rest(), interact.interaction.NoInteractionFound(), crafting.Craft(), system.item.ItemSpawner(), travel.climb.ClimbComplete(), combat.approach.StartMove(), combat.dodge.Dodge(), core.gameState.quests.CompleteQuest(), interact.actions.StartFire(), travel.ViewRoute(), status.statChanged.PlayerStatMaxed(), interact.actions.UseOnFire(), status.LevelUp(), system.TurnListener(), status.statChanged.CreatureDied(), status.effects.ApplyEffects(), crafting.CheckRecipes(), combat.approach.Move(), travel.jump.PlayerJump(), combat.attack.StartAttack(), interact.actions.NoUseFound(), explore.map.ReadMap(), travel.jump.PlayerFall(), interact.actions.ChopWood(), status.statChanged.StatMinned(), inventory.dropItem.ItemDropped(), system.gameTick.GameTick(), combat.battle.BattleTick(), system.help.ViewHelp(), inventory.unEquipItem.ItemUnEquipped(), status.journal.ViewQuestList(), interact.eat.EatFood(), inventory.ListInventory(), interact.magic.ViewWordHelp(), interact.scope.spawn.ActivatorSpawner(), interact.scope.spawn.SpawnItem(), travel.TravelStart(), inventory.pickupItem.ItemPickedUp(), inventory.unEquipItem.UnEquipItem(), travel.climb.AttemptClimb(), status.statChanged.StatBoosted(), system.debug.DebugStatListener(), interact.actions.UseFoodItem(), system.persistance.saving.Save())

