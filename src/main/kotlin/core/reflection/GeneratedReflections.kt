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

private val commands: List<core.commands.Command> = listOf(combat.dodge.DodgeCommand(), system.debug.DebugCommand(), explore.map.ReadMapCommand(), traveling.climb.DismountCommand(), crafting.craft.CookCommand(), inventory.EquippedCommand(), system.persistance.newGame.CreateNewGameCommand(), explore.LookCommand(), crafting.checkRecipe.RecipeCommand(), system.help.CommandsCommand(), combat.attack.AttackCommand(), system.history.HistoryCommand(), inventory.equipItem.EquipItemCommand(), inventory.unEquipItem.UnEquipItemCommand(), inventory.InventoryCommand(), traveling.travel.TravelInDirectionCommand(), core.commands.UnknownCommand(), combat.block.BlockCommand(), traveling.travel.TravelCommand(), system.help.HelpCommand(), status.status.StatusCommand(), combat.approach.RetreatCommand(), traveling.climb.ClimbCommand(), traveling.routes.RouteCommand(), magic.castSpell.CastCommand(), traveling.move.MoveCommand(), status.rest.RestCommand(), inventory.pickupItem.PickupItemCommand(), system.RedoCommand(), crafting.craft.CraftRecipeCommand(), time.ViewTimeCommand(), use.eat.EatCommand(), traveling.jump.JumpCommand(), inventory.dropItem.PlaceItemCommand(), system.persistance.saving.SaveCommand(), quests.journal.JournalCommand(), system.persistance.changePlayer.PlayAsCommand(), combat.approach.ApproachCommand(), use.UseCommand(), system.ExitCommand(), use.interaction.nothing.NothingCommand(), system.persistance.loading.LoadCommand())

private val spellCommands: List<magic.spellCommands.SpellCommand> = listOf(magic.spellCommands.water.Poison(), magic.spellCommands.air.Push(), magic.spellCommands.water.Jet(), magic.spellCommands.air.Adrenaline(), magic.spellCommands.water.Heal(), magic.spellCommands.fire.Flame(), magic.spellCommands.earth.Rock(), magic.spellCommands.air.Pull(), magic.spellCommands.earth.Rooted())

private val eventParsers: List<core.events.eventParsers.EventParser> = listOf(core.events.eventParsers.SpawnItemEventParser(), core.events.eventParsers.ArriveEventParser(), core.events.eventParsers.AddConditionEventParser(), core.events.eventParsers.RestrictLocationEventParser(), core.events.eventParsers.DiscoverRecipeEventParser(), core.events.eventParsers.CompleteQuestEventParser(), core.events.eventParsers.RemoveScopeEventParser(), core.events.eventParsers.RemoveItemEventParser(), core.events.eventParsers.SetQuestStageEventParser(), core.events.eventParsers.SpawnActivatorEventParser(), core.events.eventParsers.StatChangeEventParser(), core.events.eventParsers.RemoveConditionEventParser(), core.events.eventParsers.SetPropertiesEventParser(), core.events.eventParsers.MessageEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(core.history.SessionListener(), inventory.equipItem.EquipItem(), traveling.routes.FindRoute(), magic.castSpell.StartCastSpell(), system.persistance.changePlayer.ListCharacters(), traveling.arrive.Arrive(), system.persistance.loading.ListSaves(), use.actions.StartFire(), system.help.ViewHelp(), system.persistance.saving.Save(), traveling.scope.spawn.ActivatorSpawner(), use.interaction.nothing.DoNothing(), use.interaction.Interact(), traveling.jump.PlayerFall(), use.eat.EatFood(), status.rest.Rest(), use.actions.ChopWood(), traveling.scope.remove.RemoveScope(), core.target.item.ItemSpawner(), use.actions.UseIngredientOnActivatorRecipe(), combat.battle.BattleTick(), core.properties.propValChanged.PropertyStatMinned(), status.statChanged.StatChanged(), magic.castSpell.CastSpell(), use.actions.DamageCreature(), traveling.routes.ViewRoute(), system.persistance.newGame.CreateNewGame(), system.history.ViewChatHistory(), quests.SetQuestStage(), combat.dodge.Dodge(), status.statChanged.PlayerStatMaxed(), status.LevelUp(), inventory.unEquipItem.UnEquipItem(), status.ExpGained(), system.persistance.loading.Load(), traveling.climb.AttemptClimb(), status.conditions.RemoveCondition(), status.statChanged.StatBoosted(), traveling.location.weather.WeatherListener(), use.interaction.nothing.StartNothing(), time.gameTick.GameTick(), explore.map.ReadMap(), quests.journal.ViewQuestList(), system.message.DisplayMessage(), use.actions.NoUseFound(), combat.battle.BattleTurn(), status.statChanged.StatMinned(), combat.approach.StartMove(), combat.block.StartBlock(), inventory.ListInventory(), traveling.jump.PlayerJump(), system.debug.DebugStatListener(), inventory.dropItem.ItemDropped(), inventory.equipItem.ItemEquipped(), core.properties.SetProperties(), status.statChanged.CreatureDied(), quests.QuestListener(), combat.dodge.StartDodge(), use.interaction.NoInteractionFound(), status.effects.ApplyEffects(), combat.attack.StartAttack(), crafting.craft.Craft(), crafting.checkRecipe.CheckRecipes(), system.debug.DebugListListener(), traveling.climb.ClimbComplete(), time.ViewTime(), system.debug.DebugToggleListener(), use.actions.UseOnFire(), traveling.scope.spawn.SpawnItem(), inventory.pickupItem.ItemPickedUp(), traveling.travel.TravelStart(), status.status.Status(), combat.battle.CombatantDied(), crafting.DiscoverRecipe(), system.persistance.changePlayer.PlayAs(), use.actions.UseFoodItem(), system.debug.DebugTagListener(), combat.attack.Attack(), magic.ViewWordHelp(), core.properties.propValChanged.PropertyStatChanged(), core.MessageHandler(), use.actions.UseItemOnIngredientRecipe(), quests.journal.ViewQuestJournal(), status.statChanged.PlayerStatMinned(), traveling.arrive.ArrivalHandler(), explore.Look(), inventory.dropItem.TransferItem(), combat.block.Block(), traveling.move.Move(), core.TurnListener(), use.actions.ScratchSurface(), traveling.scope.remove.RemoveItem(), inventory.unEquipItem.ItemUnEquipped(), combat.approach.Move(), time.gameTick.TimeListener(), traveling.RestrictLocation(), quests.CompleteQuest(), combat.takeDamage.TakeDamage(), status.conditions.AddCondition())

