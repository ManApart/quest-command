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

private val commands: List<core.commands.Command> = listOf(core.commands.UnknownCommand(), traveling.move.MoveCommand(), system.RedoCommand(), combat.dodge.DodgeCommand(), traveling.climb.DismountCommand(), status.rest.RestCommand(), inventory.unEquipItem.UnEquipItemCommand(), system.help.HelpCommand(), traveling.climb.ClimbCommand(), use.UseCommand(), traveling.travel.TravelCommand(), system.debug.DebugCommand(), system.ExitCommand(), inventory.equipItem.EquipItemCommand(), explore.map.ReadMapCommand(), crafting.checkRecipe.RecipeCommand(), inventory.dropItem.PlaceItemCommand(), system.persistance.saving.SaveCommand(), traveling.routes.RouteCommand(), inventory.EquippedCommand(), explore.examine.ExamineCommand(), status.status.StatusCommand(), traveling.jump.JumpCommand(), system.persistance.loading.LoadCommand(), time.ViewTimeCommand(), combat.block.BlockCommand(), crafting.craft.CraftRecipeCommand(), system.history.HistoryCommand(), inventory.pickupItem.PickupItemCommand(), traveling.travel.TravelInDirectionCommand(), combat.approach.ApproachCommand(), inventory.InventoryCommand(), use.eat.EatCommand(), crafting.craft.CookCommand(), system.help.CommandsCommand(), explore.look.LookCommand(), quests.journal.JournalCommand(), system.persistance.changePlayer.PlayAsCommand(), combat.approach.RetreatCommand(), system.persistance.newGame.CreateNewGameCommand(), use.interaction.nothing.NothingCommand(), magic.castSpell.CastCommand(), combat.attack.AttackCommand())

private val spellCommands: List<magic.spellCommands.SpellCommand> = listOf(magic.spellCommands.fire.Flame(), magic.spellCommands.water.Heal(), magic.spellCommands.air.Pull(), magic.spellCommands.earth.Rooted(), magic.spellCommands.air.Adrenaline(), magic.spellCommands.earth.Rock(), magic.spellCommands.air.Push(), magic.spellCommands.water.Poison(), magic.spellCommands.water.Jet())

private val eventParsers: List<core.events.eventParsers.EventParser> = listOf(core.events.eventParsers.ArriveEventParser(), core.events.eventParsers.SetQuestStageEventParser(), core.events.eventParsers.RemoveItemEventParser(), core.events.eventParsers.SetPropertiesEventParser(), core.events.eventParsers.RemoveScopeEventParser(), core.events.eventParsers.StatChangeEventParser(), core.events.eventParsers.RestrictLocationEventParser(), core.events.eventParsers.CompleteQuestEventParser(), core.events.eventParsers.MessageEventParser(), core.events.eventParsers.SpawnActivatorEventParser(), core.events.eventParsers.DiscoverRecipeEventParser(), core.events.eventParsers.SpawnItemEventParser(), core.events.eventParsers.AddConditionEventParser(), core.events.eventParsers.RemoveConditionEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(use.actions.DamageCreature(), use.actions.StartFire(), use.interaction.nothing.StartNothing(), core.history.SessionListener(), traveling.routes.ViewRoute(), core.properties.propValChanged.PropertyStatChanged(), traveling.climb.AttemptClimb(), inventory.dropItem.ItemDropped(), use.actions.ScratchSurface(), inventory.pickupItem.ItemPickedUp(), combat.approach.StartMove(), status.rest.Rest(), inventory.unEquipItem.ItemUnEquipped(), combat.battle.BattleTurn(), use.actions.UseItemOnIngredientRecipe(), system.debug.DebugStatListener(), status.statChanged.StatMinned(), core.properties.SetProperties(), status.conditions.RemoveCondition(), traveling.arrive.ArrivalHandler(), time.ViewTime(), system.persistance.saving.Save(), combat.dodge.Dodge(), explore.map.ReadMap(), combat.takeDamage.TakeDamage(), combat.attack.StartAttack(), use.interaction.NoInteractionFound(), system.persistance.newGame.CreateNewGame(), explore.look.Look(), time.gameTick.TimeListener(), status.statChanged.StatBoosted(), core.properties.propValChanged.PropertyStatMinned(), magic.ViewWordHelp(), status.effects.ApplyEffects(), explore.examine.Examine(), inventory.equipItem.ItemEquipped(), combat.battle.CombatantDied(), system.persistance.loading.ListSaves(), system.persistance.changePlayer.PlayAs(), crafting.craft.Craft(), use.eat.EatFood(), quests.CompleteQuest(), traveling.travel.TravelStart(), status.LevelUp(), quests.journal.ViewQuestList(), crafting.checkRecipe.CheckRecipes(), traveling.routes.FindRoute(), traveling.RestrictLocation(), system.debug.DebugListListener(), system.help.ViewHelp(), inventory.unEquipItem.UnEquipItem(), status.statChanged.PlayerStatMaxed(), traveling.scope.remove.RemoveScope(), combat.attack.Attack(), traveling.move.Move(), system.debug.DebugToggleListener(), inventory.equipItem.EquipItem(), combat.dodge.StartDodge(), status.ExpGained(), traveling.jump.PlayerFall(), traveling.scope.remove.RemoveItem(), use.actions.ChopWood(), core.MessageHandler(), system.debug.DebugTagListener(), traveling.scope.spawn.SpawnItem(), use.actions.UseOnFire(), combat.battle.BattleTick(), combat.block.Block(), use.actions.UseFoodItem(), traveling.scope.spawn.ActivatorSpawner(), use.interaction.Interact(), traveling.jump.PlayerJump(), system.persistance.loading.Load(), system.message.DisplayMessage(), quests.journal.ViewQuestJournal(), time.gameTick.GameTick(), use.actions.UseIngredientOnActivatorRecipe(), status.statChanged.StatChanged(), magic.castSpell.CastSpell(), magic.castSpell.StartCastSpell(), system.persistance.changePlayer.ListCharacters(), core.target.item.ItemSpawner(), traveling.location.weather.WeatherListener(), use.actions.NoUseFound(), status.statChanged.CreatureDied(), crafting.DiscoverRecipe(), traveling.arrive.Arrive(), inventory.ListInventory(), status.statChanged.PlayerStatMinned(), inventory.dropItem.TransferItem(), combat.approach.Move(), combat.block.StartBlock(), system.history.ViewChatHistory(), system.debug.DebugWeatherListener(), core.TurnListener(), traveling.climb.ClimbComplete(), quests.QuestListener(), status.status.Status(), status.conditions.AddCondition(), use.interaction.nothing.DoNothing(), quests.SetQuestStage())

