package core.utility.reflection

import core.commands.Command
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

private val eventListeners: List<core.events.EventListener<*>> = listOf(combat.block.Block(), system.TurnListener(), status.propValChanged.PropertyStatChanged(), system.gameTick.GameTick(), combat.approach.StartMove(), interact.actions.ScratchSurface(), interact.actions.UseFoodItem(), inventory.unEquipItem.UnEquipItem(), crafting.CheckRecipes(), status.statChanged.PlayerStatMaxed(), travel.Arrive(), system.debug.DebugListListener(), system.persistance.saving.Save(), status.Status(), core.gameState.SetProperties(), combat.battle.BattleTick(), system.debug.DebugStatListener(), combat.battle.BattleTurn(), core.gameState.quests.QuestListener(), inventory.dropItem.TransferItem(), interact.scope.remove.RemoveScope(), system.gameTick.TimeManager(), travel.move.Move(), crafting.DiscoverRecipe(), interact.actions.DamageCreature(), inventory.equipItem.EquipItem(), interact.magic.CastSpell(), interact.scope.remove.RemoveItem(), interact.interaction.Interact(), core.history.SessionListener(), interact.magic.StartCastSpell(), system.help.ViewHelp(), combat.takeDamage.TakeDamage(), status.statChanged.StatChanged(), interact.actions.NoUseFound(), interact.interaction.nothing.StartNothing(), status.statChanged.PlayerStatMinned(), travel.ViewRoute(), status.statChanged.CreatureDied(), travel.TravelStart(), explore.RestrictLocation(), interact.scope.spawn.ActivatorSpawner(), interact.actions.StartFire(), crafting.Craft(), status.statChanged.StatBoosted(), interact.actions.ChopWood(), inventory.unEquipItem.ItemUnEquipped(), travel.climb.ClimbComplete(), status.rest.Rest(), interact.interaction.NoInteractionFound(), interact.actions.UseItemOnIngredientRecipe(), status.LevelUp(), combat.attack.Attack(), system.history.ViewChatHistory(), system.item.ItemSpawner(), interact.interaction.nothing.DoNothing(), travel.FindRoute(), combat.attack.StartAttack(), interact.magic.ViewWordHelp(), travel.jump.PlayerFall(), system.message.DisplayMessage(), interact.scope.ArrivalHandler(), inventory.ListInventory(), status.effects.ApplyEffects(), interact.actions.UseOnFire(), inventory.equipItem.ItemEquipped(), interact.scope.spawn.SpawnItem(), status.propValChanged.PropertyStatMinned(), system.debug.DebugToggleListener(), system.debug.DebugTagListener(), combat.approach.Move(), combat.dodge.Dodge(), status.journal.ViewQuestJournal(), system.GameManager.MessageHandler(), core.gameState.quests.SetQuestStage(), travel.jump.PlayerJump(), combat.block.StartBlock(), status.ExpGained(), combat.battle.CombatantDied(), status.statChanged.StatMinned(), combat.dodge.StartDodge(), explore.map.ReadMap(), interact.eat.EatFood(), status.journal.ViewQuestList(), inventory.pickupItem.ItemPickedUp(), core.gameState.quests.CompleteQuest(), explore.Look(), travel.climb.AttemptClimb(), status.effects.RemoveCondition(), status.effects.AddCondition(), inventory.dropItem.ItemDropped(), interact.actions.UseIngredientOnActivatorRecipe())

