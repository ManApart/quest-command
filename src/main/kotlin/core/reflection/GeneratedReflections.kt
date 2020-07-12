package core.reflection

import core.commands.Command
import core.events.EventListener
import core.events.eventParsers.EventParser
import magic.spellCommands.SpellCommand

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

private val commands: List<core.commands.Command> = listOf(combat.attack.AttackCommand(), combat.block.BlockCommand(), combat.dodge.DodgeCommand(), core.commands.UnknownCommand(), crafting.checkRecipe.RecipeCommand(), crafting.craft.CookCommand(), crafting.craft.CraftRecipeCommand(), explore.examine.ExamineCommand(), explore.look.LookCommand(), explore.map.ReadMapCommand(), inventory.EquippedCommand(), inventory.InventoryCommand(), inventory.dropItem.PlaceItemCommand(), inventory.equipItem.EquipItemCommand(), inventory.pickupItem.PickupItemCommand(), inventory.unEquipItem.UnEquipItemCommand(), magic.castSpell.CastCommand(), quests.journal.JournalCommand(), status.rest.RestCommand(), status.status.StatusCommand(), system.ExitCommand(), system.RedoCommand(), system.debug.DebugCommand(), system.help.CommandsCommand(), system.help.HelpCommand(), system.history.HistoryCommand(), system.persistance.changePlayer.PlayAsCommand(), system.persistance.loading.LoadCommand(), system.persistance.newGame.CreateNewGameCommand(), system.persistance.saving.SaveCommand(), time.ViewTimeCommand(), traveling.approach.ApproachCommand(), traveling.approach.RetreatCommand(), traveling.climb.ClimbCommand(), traveling.climb.DismountCommand(), traveling.jump.JumpCommand(), traveling.move.MoveCommand(), traveling.routes.RouteCommand(), traveling.travel.TravelCommand(), traveling.travel.TravelInDirectionCommand(), use.UseCommand(), use.eat.EatCommand(), use.interaction.nothing.NothingCommand())

private val spellCommands: List<magic.spellCommands.SpellCommand> = listOf(magic.spellCommands.air.Adrenaline(), magic.spellCommands.air.Pull(), magic.spellCommands.air.Push(), magic.spellCommands.earth.Rock(), magic.spellCommands.earth.Rooted(), magic.spellCommands.fire.Flame(), magic.spellCommands.water.Heal(), magic.spellCommands.water.Jet(), magic.spellCommands.water.Poison())

private val eventParsers: List<core.events.eventParsers.EventParser> = listOf(core.events.eventParsers.AddConditionEventParser(), core.events.eventParsers.ArriveEventParser(), core.events.eventParsers.CommandEventParser(), core.events.eventParsers.CompleteQuestEventParser(), core.events.eventParsers.DiscoverRecipeEventParser(), core.events.eventParsers.MessageEventParser(), core.events.eventParsers.RemoveConditionEventParser(), core.events.eventParsers.RemoveItemEventParser(), core.events.eventParsers.RemoveScopeEventParser(), core.events.eventParsers.RestrictLocationEventParser(), core.events.eventParsers.SetPropertiesEventParser(), core.events.eventParsers.SetQuestStageEventParser(), core.events.eventParsers.SpawnActivatorEventParser(), core.events.eventParsers.SpawnItemEventParser(), core.events.eventParsers.StatChangeEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(combat.attack.Attack(), combat.block.Block(), combat.takeDamage.TakeDamage(), core.MessageHandler(), core.TurnListener(), core.ai.AITurnDirector(), core.ai.DelayedEventListener(), core.commands.commandEvent.CommandEventListener(), core.events.multiEvent.MultiEventListener(), core.history.SessionListener(), core.properties.SetProperties(), core.properties.propValChanged.PropertyStatChanged(), core.properties.propValChanged.PropertyStatMinned(), core.target.item.ItemSpawner(), crafting.DiscoverRecipe(), crafting.checkRecipe.CheckRecipes(), crafting.craft.Craft(), explore.examine.Examine(), explore.look.Look(), explore.map.ReadMap(), inventory.ListInventory(), inventory.dropItem.ItemDropped(), inventory.dropItem.TransferItem(), inventory.equipItem.EquipItem(), inventory.equipItem.ItemEquipped(), inventory.pickupItem.ItemPickedUp(), inventory.unEquipItem.ItemUnEquipped(), inventory.unEquipItem.UnEquipItem(), magic.ViewWordHelp(), magic.castSpell.CastSpell(), quests.CompleteQuest(), quests.QuestListener(), quests.SetQuestStage(), quests.journal.ViewQuestJournal(), quests.journal.ViewQuestList(), status.ExpGained(), status.LevelUp(), status.conditions.AddCondition(), status.conditions.RemoveCondition(), status.effects.ApplyEffects(), status.rest.Rest(), status.statChanged.CreatureDied(), status.statChanged.PlayerStatMaxed(), status.statChanged.PlayerStatMinned(), status.statChanged.StatBoosted(), status.statChanged.StatChanged(), status.statChanged.StatMinned(), status.status.Status(), system.debug.DebugListListener(), system.debug.DebugStatListener(), system.debug.DebugTagListener(), system.debug.DebugToggleListener(), system.debug.DebugWeatherListener(), system.help.ViewHelp(), system.history.ViewChatHistory(), system.message.DisplayMessage(), system.persistance.changePlayer.ListCharacters(), system.persistance.changePlayer.PlayAs(), system.persistance.loading.ListSaves(), system.persistance.loading.Load(), system.persistance.newGame.CreateNewGame(), system.persistance.saving.Save(), time.ViewTime(), time.gameTick.GameTick(), time.gameTick.TimeListener(), traveling.RestrictLocation(), traveling.arrive.ArrivalHandler(), traveling.arrive.Arrive(), traveling.climb.AttemptClimb(), traveling.climb.ClimbComplete(), traveling.jump.PlayerFall(), traveling.jump.PlayerJump(), traveling.location.weather.WeatherListener(), traveling.move.Move(), traveling.routes.FindRoute(), traveling.routes.ViewRoute(), traveling.scope.remove.RemoveItem(), traveling.scope.remove.RemoveScope(), traveling.scope.spawn.ActivatorSpawner(), traveling.scope.spawn.SpawnItem(), traveling.travel.TravelStart(), use.actions.ChopWood(), use.actions.DamageCreature(), use.actions.NoUseFound(), use.actions.ScratchSurface(), use.actions.StartFire(), use.actions.UseFoodItem(), use.actions.UseIngredientOnActivatorRecipe(), use.actions.UseItemOnIngredientRecipe(), use.actions.UseOnFire(), use.eat.EatFood(), use.interaction.Interact(), use.interaction.NoInteractionFound(), use.interaction.nothing.DoNothing())

