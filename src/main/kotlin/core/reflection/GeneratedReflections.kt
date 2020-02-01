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

private val eventListeners: List<core.events.EventListener<*>> = listOf(core.history.SessionListener(), use.actions.UseItemOnIngredientRecipe(), use.interaction.nothing.StartNothing(), traveling.routes.FindRoute(), core.properties.propValChanged.PropertyStatMinned(), core.properties.propValChanged.PropertyStatChanged(), traveling.climb.AttemptClimb(), traveling.travel.TravelStart(), use.actions.NoUseFound(), system.help.ViewHelp(), explore.look.Look(), status.rest.Rest(), use.interaction.nothing.DoNothing(), use.interaction.Interact(), use.actions.UseOnFire(), system.debug.DebugStatListener(), status.statChanged.StatMinned(), core.properties.SetProperties(), status.statChanged.StatChanged(), traveling.arrive.ArrivalHandler(), time.ViewTime(), system.history.ViewChatHistory(), combat.dodge.Dodge(), traveling.routes.ViewRoute(), status.conditions.RemoveCondition(), status.ExpGained(), use.actions.DamageCreature(), system.persistance.newGame.CreateNewGame(), core.MessageHandler(), time.gameTick.TimeListener(), status.statChanged.StatBoosted(), magic.castSpell.StartCastSpell(), magic.ViewWordHelp(), status.effects.ApplyEffects(), combat.takeDamage.TakeDamage(), inventory.equipItem.ItemEquipped(), combat.battle.CombatantDied(), explore.map.ReadMap(), system.persistance.changePlayer.PlayAs(), system.debug.DebugListListener(), use.eat.EatFood(), quests.CompleteQuest(), system.persistance.loading.ListSaves(), status.LevelUp(), quests.journal.ViewQuestList(), use.actions.UseIngredientOnActivatorRecipe(), combat.battle.BattleTurn(), traveling.RestrictLocation(), traveling.location.weather.WeatherListener(), combat.block.StartBlock(), inventory.unEquipItem.UnEquipItem(), combat.attack.Attack(), traveling.scope.remove.RemoveScope(), traveling.jump.PlayerJump(), traveling.move.Move(), system.debug.DebugToggleListener(), inventory.equipItem.EquipItem(), combat.dodge.StartDodge(), magic.castSpell.CastSpell(), traveling.jump.PlayerFall(), combat.attack.StartAttack(), system.debug.DebugTagListener(), system.persistance.saving.Save(), crafting.craft.Craft(), traveling.scope.spawn.SpawnItem(), combat.battle.BattleTick(), combat.block.Block(), use.actions.StartFire(), traveling.scope.spawn.ActivatorSpawner(), inventory.pickupItem.ItemPickedUp(), inventory.dropItem.ItemDropped(), system.persistance.loading.Load(), system.message.DisplayMessage(), quests.journal.ViewQuestJournal(), time.gameTick.GameTick(), use.actions.ScratchSurface(), use.actions.ChopWood(), use.interaction.NoInteractionFound(), status.statChanged.PlayerStatMaxed(), system.persistance.changePlayer.ListCharacters(), core.target.item.ItemSpawner(), combat.approach.StartMove(), use.actions.UseFoodItem(), status.statChanged.CreatureDied(), crafting.DiscoverRecipe(), traveling.arrive.Arrive(), inventory.ListInventory(), status.statChanged.PlayerStatMinned(), inventory.dropItem.TransferItem(), combat.approach.Move(), core.TurnListener(), crafting.checkRecipe.CheckRecipes(), traveling.scope.remove.RemoveItem(), inventory.unEquipItem.ItemUnEquipped(), traveling.climb.ClimbComplete(), quests.QuestListener(), status.status.Status(), status.conditions.AddCondition(), explore.examine.Examine(), quests.SetQuestStage())

