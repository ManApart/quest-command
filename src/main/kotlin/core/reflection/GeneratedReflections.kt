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

private val commands: List<core.commands.Command> = listOf(traveling.travel.TravelCommand(), crafting.craft.CookCommand(), inventory.equipItem.EquipItemCommand(), system.RedoCommand(), system.help.HelpCommand(), inventory.dropItem.PlaceItemCommand(), traveling.move.MoveCommand(), status.rest.RestCommand(), combat.attack.AttackCommand(), explore.LookCommand(), status.status.StatusCommand(), inventory.EquippedCommand(), combat.approach.ApproachCommand(), combat.block.BlockCommand(), crafting.craft.CraftRecipeCommand(), combat.dodge.DodgeCommand(), use.UseCommand(), magic.castSpell.CastCommand(), explore.map.ReadMapCommand(), combat.approach.RetreatCommand(), quests.journal.JournalCommand(), crafting.checkRecipe.RecipeCommand(), use.eat.EatCommand(), traveling.climb.ClimbCommand(), system.help.CommandsCommand(), traveling.climb.DismountCommand(), inventory.InventoryCommand(), inventory.pickupItem.PickupItemCommand(), use.interaction.nothing.NothingCommand(), traveling.jump.JumpCommand(), system.history.HistoryCommand(), system.persistance.saving.SaveCommand(), system.ExitCommand(), core.commands.UnknownCommand(), traveling.routes.RouteCommand(), traveling.travel.TravelInDirectionCommand(), inventory.unEquipItem.UnEquipItemCommand(), system.debug.DebugCommand())

private val spellCommands: List<magic.spellCommands.SpellCommand> = listOf(magic.spellCommands.water.Poison(), magic.spellCommands.earth.Rooted(), magic.spellCommands.fire.Flame(), magic.spellCommands.air.Adrenaline(), magic.spellCommands.air.Push(), magic.spellCommands.earth.Rock(), magic.spellCommands.air.Pull(), magic.spellCommands.water.Jet(), magic.spellCommands.water.Heal())

private val eventParsers: List<core.events.eventParsers.EventParser> = listOf(core.events.eventParsers.SpawnActivatorEventParser(), core.events.eventParsers.CompleteQuestEventParser(), core.events.eventParsers.ArriveEventParser(), core.events.eventParsers.StatChangeEventParser(), core.events.eventParsers.SetQuestStageEventParser(), core.events.eventParsers.RemoveItemEventParser(), core.events.eventParsers.SpawnItemEventParser(), core.events.eventParsers.DiscoverRecipeEventParser(), core.events.eventParsers.RestrictLocationEventParser(), core.events.eventParsers.MessageEventParser(), core.events.eventParsers.SetPropertiesEventParser(), core.events.eventParsers.RemoveScopeEventParser(), core.events.eventParsers.RemoveConditionEventParser(), core.events.eventParsers.AddConditionEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(combat.block.Block(), quests.QuestListener(), traveling.arrive.Arrive(), status.conditions.RemoveCondition(), system.debug.DebugListListener(), use.actions.UseOnFire(), use.actions.ScratchSurface(), status.effects.ApplyEffects(), traveling.scope.remove.RemoveScope(), status.rest.Rest(), system.debug.DebugTagListener(), system.history.ViewChatHistory(), combat.attack.StartAttack(), combat.attack.Attack(), combat.battle.BattleTick(), traveling.arrive.ArrivalHandler(), use.interaction.Interact(), combat.battle.CombatantDied(), traveling.routes.ViewRoute(), inventory.dropItem.TransferItem(), inventory.equipItem.EquipItem(), inventory.dropItem.ItemDropped(), explore.map.ReadMap(), quests.journal.ViewQuestJournal(), quests.SetQuestStage(), use.interaction.nothing.StartNothing(), system.persistance.saving.Save(), magic.castSpell.StartCastSpell(), use.actions.ChopWood(), core.history.SessionListener(), traveling.climb.ClimbComplete(), system.help.ViewHelp(), combat.takeDamage.TakeDamage(), traveling.travel.TravelStart(), use.actions.StartFire(), status.statChanged.PlayerStatMinned(), crafting.DiscoverRecipe(), combat.dodge.StartDodge(), status.statChanged.CreatureDied(), traveling.routes.FindRoute(), use.interaction.NoInteractionFound(), status.status.Status(), use.actions.UseFoodItem(), combat.approach.StartMove(), quests.CompleteQuest(), time.gameTick.GameTick(), inventory.unEquipItem.ItemUnEquipped(), magic.castSpell.CastSpell(), core.properties.SetProperties(), traveling.scope.spawn.ActivatorSpawner(), use.actions.UseIngredientOnActivatorRecipe(), magic.ViewWordHelp(), core.properties.propValChanged.PropertyStatMinned(), crafting.checkRecipe.CheckRecipes(), core.target.item.ItemSpawner(), use.interaction.nothing.DoNothing(), traveling.scope.remove.RemoveItem(), use.actions.DamageCreature(), status.statChanged.PlayerStatMaxed(), combat.approach.Move(), quests.journal.ViewQuestList(), status.conditions.AddCondition(), inventory.unEquipItem.UnEquipItem(), traveling.jump.PlayerFall(), use.actions.UseItemOnIngredientRecipe(), inventory.equipItem.ItemEquipped(), status.LevelUp(), combat.battle.BattleTurn(), system.debug.DebugToggleListener(), status.statChanged.StatChanged(), traveling.move.Move(), combat.dodge.Dodge(), use.eat.EatFood(), traveling.jump.PlayerJump(), crafting.craft.Craft(), core.properties.propValChanged.PropertyStatChanged(), core.TurnListener(), status.ExpGained(), traveling.climb.AttemptClimb(), status.statChanged.StatMinned(), explore.Look(), time.gameTick.TimeManager(), traveling.RestrictLocation(), system.message.DisplayMessage(), inventory.pickupItem.ItemPickedUp(), combat.block.StartBlock(), inventory.ListInventory(), traveling.scope.spawn.SpawnItem(), status.statChanged.StatBoosted(), system.debug.DebugStatListener(), core.GameManager.MessageHandler(), use.actions.NoUseFound())

