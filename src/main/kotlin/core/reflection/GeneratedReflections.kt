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

private val commands: List<core.commands.Command> = listOf(traveling.climb.ClimbCommand(), combat.dodge.DodgeCommand(), use.eat.EatCommand(), inventory.EquippedCommand(), system.persistance.saving.SaveCommand(), explore.LookCommand(), combat.approach.RetreatCommand(), system.persistance.loading.LoadCommand(), traveling.move.MoveCommand(), use.interaction.nothing.NothingCommand(), inventory.pickupItem.PickupItemCommand(), system.ExitCommand(), inventory.unEquipItem.UnEquipItemCommand(), traveling.jump.JumpCommand(), combat.approach.ApproachCommand(), crafting.checkRecipe.RecipeCommand(), quests.journal.JournalCommand(), system.help.HelpCommand(), traveling.routes.RouteCommand(), traveling.travel.TravelCommand(), crafting.craft.CookCommand(), inventory.dropItem.PlaceItemCommand(), inventory.equipItem.EquipItemCommand(), system.persistance.newGame.CreateNewGameCommand(), explore.map.ReadMapCommand(), magic.castSpell.CastCommand(), system.history.HistoryCommand(), core.commands.UnknownCommand(), use.UseCommand(), traveling.travel.TravelInDirectionCommand(), system.RedoCommand(), combat.block.BlockCommand(), traveling.climb.DismountCommand(), combat.attack.AttackCommand(), system.persistance.changePlayer.PlayAsCommand(), system.debug.DebugCommand(), status.status.StatusCommand(), inventory.InventoryCommand(), status.rest.RestCommand(), system.help.CommandsCommand(), crafting.craft.CraftRecipeCommand())

private val spellCommands: List<magic.spellCommands.SpellCommand> = listOf(magic.spellCommands.air.Adrenaline(), magic.spellCommands.water.Jet(), magic.spellCommands.water.Heal(), magic.spellCommands.earth.Rooted(), magic.spellCommands.water.Poison(), magic.spellCommands.air.Pull(), magic.spellCommands.fire.Flame(), magic.spellCommands.air.Push(), magic.spellCommands.earth.Rock())

private val eventParsers: List<core.events.eventParsers.EventParser> = listOf(core.events.eventParsers.RemoveScopeEventParser(), core.events.eventParsers.RestrictLocationEventParser(), core.events.eventParsers.ArriveEventParser(), core.events.eventParsers.SetQuestStageEventParser(), core.events.eventParsers.DiscoverRecipeEventParser(), core.events.eventParsers.RemoveConditionEventParser(), core.events.eventParsers.RemoveItemEventParser(), core.events.eventParsers.StatChangeEventParser(), core.events.eventParsers.SetPropertiesEventParser(), core.events.eventParsers.SpawnItemEventParser(), core.events.eventParsers.SpawnActivatorEventParser(), core.events.eventParsers.CompleteQuestEventParser(), core.events.eventParsers.MessageEventParser(), core.events.eventParsers.AddConditionEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(time.gameTick.TimeManager(), system.message.DisplayMessage(), magic.ViewWordHelp(), system.help.ViewHelp(), status.statChanged.PlayerStatMinned(), traveling.jump.PlayerJump(), explore.map.ReadMap(), system.debug.DebugStatListener(), system.persistance.saving.Save(), explore.Look(), combat.block.StartBlock(), use.actions.UseItemOnIngredientRecipe(), use.interaction.NoInteractionFound(), core.properties.propValChanged.PropertyStatMinned(), use.actions.NoUseFound(), use.actions.UseOnFire(), combat.attack.Attack(), status.effects.ApplyEffects(), combat.dodge.StartDodge(), traveling.routes.FindRoute(), use.interaction.nothing.StartNothing(), use.actions.DamageCreature(), use.interaction.Interact(), use.actions.UseIngredientOnActivatorRecipe(), inventory.dropItem.TransferItem(), status.statChanged.StatChanged(), core.TurnListener(), status.conditions.AddCondition(), core.history.SessionListener(), traveling.jump.PlayerFall(), core.properties.SetProperties(), status.statChanged.StatBoosted(), status.statChanged.StatMinned(), use.actions.ScratchSurface(), system.persistance.changePlayer.PlayAs(), quests.journal.ViewQuestJournal(), status.statChanged.PlayerStatMaxed(), combat.battle.BattleTurn(), use.actions.UseFoodItem(), traveling.climb.AttemptClimb(), inventory.equipItem.EquipItem(), status.statChanged.CreatureDied(), status.status.Status(), core.properties.propValChanged.PropertyStatChanged(), magic.castSpell.StartCastSpell(), core.MessageHandler(), use.actions.ChopWood(), crafting.checkRecipe.CheckRecipes(), status.ExpGained(), quests.SetQuestStage(), system.persistance.changePlayer.ListCharacters(), combat.battle.BattleTick(), system.persistance.loading.Load(), traveling.RestrictLocation(), inventory.equipItem.ItemEquipped(), system.persistance.loading.ListSaves(), use.interaction.nothing.DoNothing(), system.debug.DebugToggleListener(), traveling.routes.ViewRoute(), system.debug.DebugListListener(), traveling.arrive.ArrivalHandler(), inventory.unEquipItem.UnEquipItem(), inventory.pickupItem.ItemPickedUp(), traveling.scope.spawn.SpawnItem(), crafting.craft.Craft(), traveling.travel.TravelStart(), use.eat.EatFood(), use.actions.StartFire(), crafting.DiscoverRecipe(), time.gameTick.GameTick(), status.conditions.RemoveCondition(), traveling.scope.remove.RemoveScope(), combat.takeDamage.TakeDamage(), combat.battle.CombatantDied(), quests.journal.ViewQuestList(), combat.approach.StartMove(), traveling.scope.spawn.ActivatorSpawner(), inventory.ListInventory(), status.rest.Rest(), quests.QuestListener(), traveling.move.Move(), magic.castSpell.CastSpell(), inventory.dropItem.ItemDropped(), quests.CompleteQuest(), combat.attack.StartAttack(), core.target.item.ItemSpawner(), combat.approach.Move(), traveling.arrive.Arrive(), traveling.climb.ClimbComplete(), combat.dodge.Dodge(), system.persistance.newGame.CreateNewGame(), system.history.ViewChatHistory(), traveling.scope.remove.RemoveItem(), combat.block.Block(), status.LevelUp(), inventory.unEquipItem.ItemUnEquipped(), system.debug.DebugTagListener())

