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

private val commands: List<core.commands.Command> = listOf(system.persistance.newGame.CreateNewGameCommand(), inventory.pickupItem.PickupItemCommand(), core.commands.UnknownCommand(), status.rest.RestCommand(), combat.block.BlockCommand(), system.help.HelpCommand(), traveling.travel.TravelInDirectionCommand(), system.persistance.saving.SaveCommand(), inventory.InventoryCommand(), system.ExitCommand(), system.help.CommandsCommand(), explore.LookCommand(), use.eat.EatCommand(), combat.approach.ApproachCommand(), traveling.climb.ClimbCommand(), traveling.routes.RouteCommand(), system.persistance.changePlayer.PlayAsCommand(), explore.map.ReadMapCommand(), crafting.checkRecipe.RecipeCommand(), crafting.craft.CraftRecipeCommand(), crafting.craft.CookCommand(), status.status.StatusCommand(), traveling.jump.JumpCommand(), combat.dodge.DodgeCommand(), traveling.climb.DismountCommand(), system.debug.DebugCommand(), system.history.HistoryCommand(), system.RedoCommand(), use.UseCommand(), inventory.equipItem.EquipItemCommand(), inventory.unEquipItem.UnEquipItemCommand(), inventory.dropItem.PlaceItemCommand(), traveling.move.MoveCommand(), traveling.travel.TravelCommand(), quests.journal.JournalCommand(), system.persistance.loading.LoadCommand(), inventory.EquippedCommand(), combat.approach.RetreatCommand(), combat.attack.AttackCommand(), magic.castSpell.CastCommand(), use.interaction.nothing.NothingCommand())

private val spellCommands: List<magic.spellCommands.SpellCommand> = listOf(magic.spellCommands.water.Poison(), magic.spellCommands.earth.Rock(), magic.spellCommands.air.Adrenaline(), magic.spellCommands.earth.Rooted(), magic.spellCommands.water.Jet(), magic.spellCommands.air.Push(), magic.spellCommands.air.Pull(), magic.spellCommands.fire.Flame(), magic.spellCommands.water.Heal())

private val eventParsers: List<core.events.eventParsers.EventParser> = listOf(core.events.eventParsers.SpawnActivatorEventParser(), core.events.eventParsers.SetPropertiesEventParser(), core.events.eventParsers.ArriveEventParser(), core.events.eventParsers.MessageEventParser(), core.events.eventParsers.AddConditionEventParser(), core.events.eventParsers.DiscoverRecipeEventParser(), core.events.eventParsers.RemoveConditionEventParser(), core.events.eventParsers.SpawnItemEventParser(), core.events.eventParsers.RemoveScopeEventParser(), core.events.eventParsers.SetQuestStageEventParser(), core.events.eventParsers.StatChangeEventParser(), core.events.eventParsers.RemoveItemEventParser(), core.events.eventParsers.CompleteQuestEventParser(), core.events.eventParsers.RestrictLocationEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(core.history.SessionListener(), inventory.equipItem.ItemEquipped(), use.actions.NoUseFound(), inventory.pickupItem.ItemPickedUp(), status.conditions.AddCondition(), system.help.ViewHelp(), combat.battle.CombatantDied(), inventory.dropItem.ItemDropped(), system.persistance.loading.ListSaves(), use.actions.DamageCreature(), system.history.ViewChatHistory(), traveling.routes.FindRoute(), traveling.scope.spawn.SpawnItem(), system.persistance.loading.Load(), status.statChanged.StatBoosted(), use.actions.ChopWood(), status.LevelUp(), status.conditions.RemoveCondition(), use.interaction.nothing.DoNothing(), combat.block.Block(), traveling.jump.PlayerJump(), core.properties.propValChanged.PropertyStatChanged(), traveling.travel.TravelStart(), quests.CompleteQuest(), system.message.DisplayMessage(), quests.journal.ViewQuestList(), quests.QuestListener(), status.effects.ApplyEffects(), traveling.scope.remove.RemoveItem(), system.persistance.saving.Save(), traveling.move.Move(), use.eat.EatFood(), status.statChanged.CreatureDied(), system.debug.DebugToggleListener(), magic.castSpell.StartCastSpell(), traveling.arrive.Arrive(), traveling.climb.ClimbComplete(), traveling.arrive.ArrivalHandler(), crafting.DiscoverRecipe(), use.actions.ScratchSurface(), time.gameTick.GameTick(), system.persistance.newGame.CreateNewGame(), use.actions.UseFoodItem(), use.actions.UseItemOnIngredientRecipe(), inventory.unEquipItem.UnEquipItem(), status.statChanged.StatChanged(), quests.SetQuestStage(), crafting.craft.Craft(), status.statChanged.StatMinned(), explore.map.ReadMap(), combat.approach.StartMove(), system.persistance.changePlayer.PlayAs(), combat.takeDamage.TakeDamage(), combat.attack.StartAttack(), inventory.equipItem.EquipItem(), combat.dodge.StartDodge(), traveling.routes.ViewRoute(), magic.ViewWordHelp(), status.status.Status(), inventory.ListInventory(), combat.dodge.Dodge(), system.persistance.changePlayer.ListCharacters(), core.target.item.ItemSpawner(), combat.block.StartBlock(), traveling.scope.remove.RemoveScope(), status.statChanged.PlayerStatMinned(), status.statChanged.PlayerStatMaxed(), inventory.unEquipItem.ItemUnEquipped(), combat.attack.Attack(), system.debug.DebugStatListener(), combat.battle.BattleTick(), crafting.checkRecipe.CheckRecipes(), status.rest.Rest(), use.interaction.Interact(), magic.castSpell.CastSpell(), quests.journal.ViewQuestJournal(), use.interaction.nothing.StartNothing(), core.TurnListener(), use.interaction.NoInteractionFound(), traveling.scope.spawn.ActivatorSpawner(), inventory.dropItem.TransferItem(), status.ExpGained(), use.actions.UseIngredientOnActivatorRecipe(), use.actions.UseOnFire(), combat.battle.BattleTurn(), combat.approach.Move(), core.properties.SetProperties(), system.debug.DebugTagListener(), core.properties.propValChanged.PropertyStatMinned(), use.actions.StartFire(), time.gameTick.TimeManager(), traveling.location.weather.WeatherListener(), traveling.RestrictLocation(), explore.Look(), traveling.jump.PlayerFall(), traveling.climb.AttemptClimb(), system.debug.DebugListListener(), core.MessageHandler())

