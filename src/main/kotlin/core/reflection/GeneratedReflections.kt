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

private val commands: List<core.commands.Command> = listOf(system.history.HistoryCommand(), combat.dodge.DodgeCommand(), use.eat.EatCommand(), traveling.travel.TravelCommand(), system.persistance.saving.SaveCommand(), status.rest.RestCommand(), inventory.InventoryCommand(), system.ExitCommand(), combat.block.BlockCommand(), system.persistance.loading.LoadCommand(), inventory.pickupItem.PickupItemCommand(), traveling.climb.ClimbCommand(), crafting.checkRecipe.RecipeCommand(), crafting.craft.CraftRecipeCommand(), combat.approach.RetreatCommand(), quests.journal.JournalCommand(), system.help.HelpCommand(), traveling.move.MoveCommand(), system.help.CommandsCommand(), use.UseCommand(), traveling.travel.TravelInDirectionCommand(), inventory.equipItem.EquipItemCommand(), crafting.craft.CookCommand(), inventory.unEquipItem.UnEquipItemCommand(), combat.attack.AttackCommand(), explore.LookCommand(), core.commands.UnknownCommand(), traveling.jump.JumpCommand(), system.RedoCommand(), explore.map.ReadMapCommand(), system.debug.DebugCommand(), traveling.climb.DismountCommand(), use.interaction.nothing.NothingCommand(), system.persistance.changePlayer.PlayAsCommand(), magic.castSpell.CastCommand(), status.status.StatusCommand(), combat.approach.ApproachCommand(), inventory.EquippedCommand(), inventory.dropItem.PlaceItemCommand(), traveling.routes.RouteCommand())

private val spellCommands: List<magic.spellCommands.SpellCommand> = listOf(magic.spellCommands.fire.Flame(), magic.spellCommands.earth.Rooted(), magic.spellCommands.air.Push(), magic.spellCommands.water.Heal(), magic.spellCommands.air.Pull(), magic.spellCommands.water.Jet(), magic.spellCommands.air.Adrenaline(), magic.spellCommands.water.Poison(), magic.spellCommands.earth.Rock())

private val eventParsers: List<core.events.eventParsers.EventParser> = listOf(core.events.eventParsers.DiscoverRecipeEventParser(), core.events.eventParsers.RemoveScopeEventParser(), core.events.eventParsers.SpawnItemEventParser(), core.events.eventParsers.ArriveEventParser(), core.events.eventParsers.RemoveConditionEventParser(), core.events.eventParsers.MessageEventParser(), core.events.eventParsers.AddConditionEventParser(), core.events.eventParsers.CompleteQuestEventParser(), core.events.eventParsers.RestrictLocationEventParser(), core.events.eventParsers.StatChangeEventParser(), core.events.eventParsers.RemoveItemEventParser(), core.events.eventParsers.SetQuestStageEventParser(), core.events.eventParsers.SetPropertiesEventParser(), core.events.eventParsers.SpawnActivatorEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(traveling.travel.TravelStart(), combat.battle.CombatantDied(), status.LevelUp(), core.TurnListener(), inventory.dropItem.TransferItem(), inventory.dropItem.ItemDropped(), status.conditions.RemoveCondition(), traveling.jump.PlayerFall(), crafting.checkRecipe.CheckRecipes(), combat.dodge.StartDodge(), inventory.unEquipItem.ItemUnEquipped(), traveling.routes.ViewRoute(), magic.castSpell.StartCastSpell(), use.actions.StartFire(), traveling.jump.PlayerJump(), inventory.unEquipItem.UnEquipItem(), combat.attack.StartAttack(), use.interaction.Interact(), inventory.equipItem.EquipItem(), traveling.routes.FindRoute(), system.help.ViewHelp(), use.actions.NoUseFound(), combat.block.Block(), use.actions.ChopWood(), use.interaction.nothing.DoNothing(), quests.CompleteQuest(), combat.battle.BattleTurn(), status.effects.ApplyEffects(), status.rest.Rest(), combat.dodge.Dodge(), use.eat.EatFood(), use.actions.UseFoodItem(), status.statChanged.StatBoosted(), crafting.DiscoverRecipe(), combat.attack.Attack(), inventory.pickupItem.ItemPickedUp(), use.actions.UseItemOnIngredientRecipe(), traveling.arrive.Arrive(), quests.QuestListener(), quests.journal.ViewQuestJournal(), traveling.RestrictLocation(), system.persistance.changePlayer.ListCharacters(), status.statChanged.PlayerStatMaxed(), system.history.ViewChatHistory(), system.debug.DebugTagListener(), use.actions.ScratchSurface(), use.interaction.NoInteractionFound(), status.conditions.AddCondition(), magic.ViewWordHelp(), core.target.item.ItemSpawner(), status.statChanged.CreatureDied(), status.status.Status(), status.statChanged.StatMinned(), system.persistance.loading.Load(), explore.map.ReadMap(), status.ExpGained(), core.properties.SetProperties(), core.GameManager.MessageHandler(), combat.approach.StartMove(), traveling.scope.remove.RemoveScope(), inventory.ListInventory(), combat.block.StartBlock(), traveling.climb.ClimbComplete(), system.debug.DebugListListener(), system.persistance.loading.ListSaves(), use.interaction.nothing.StartNothing(), use.actions.UseOnFire(), status.statChanged.PlayerStatMinned(), system.persistance.changePlayer.PlayAs(), status.statChanged.StatChanged(), system.debug.DebugStatListener(), magic.castSpell.CastSpell(), traveling.climb.AttemptClimb(), system.message.DisplayMessage(), system.persistance.saving.Save(), traveling.scope.spawn.SpawnItem(), explore.Look(), traveling.scope.spawn.ActivatorSpawner(), quests.SetQuestStage(), inventory.equipItem.ItemEquipped(), time.gameTick.TimeManager(), time.gameTick.GameTick(), core.history.SessionListener(), core.properties.propValChanged.PropertyStatChanged(), traveling.move.Move(), traveling.arrive.ArrivalHandler(), core.properties.propValChanged.PropertyStatMinned(), combat.approach.Move(), combat.battle.BattleTick(), traveling.scope.remove.RemoveItem(), use.actions.UseIngredientOnActivatorRecipe(), use.actions.DamageCreature(), system.debug.DebugToggleListener(), quests.journal.ViewQuestList(), combat.takeDamage.TakeDamage(), crafting.craft.Craft())

