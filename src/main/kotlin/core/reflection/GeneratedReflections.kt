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

private val commands: List<core.commands.Command> = listOf(status.rest.RestCommand(), system.help.HelpCommand(), use.eat.EatCommand(), inventory.dropItem.PlaceItemCommand(), core.commands.UnknownCommand(), traveling.travel.TravelCommand(), combat.approach.ApproachCommand(), system.history.HistoryCommand(), system.debug.DebugCommand(), traveling.climb.ClimbCommand(), inventory.pickupItem.PickupItemCommand(), explore.LookCommand(), combat.approach.RetreatCommand(), traveling.move.MoveCommand(), inventory.InventoryCommand(), system.persistance.saving.SaveCommand(), traveling.climb.DismountCommand(), combat.block.BlockCommand(), traveling.travel.TravelInDirectionCommand(), traveling.jump.JumpCommand(), explore.map.ReadMapCommand(), inventory.equipItem.EquipItemCommand(), use.UseCommand(), use.interaction.nothing.NothingCommand(), inventory.EquippedCommand(), combat.dodge.DodgeCommand(), crafting.checkRecipe.RecipeCommand(), inventory.unEquipItem.UnEquipItemCommand(), magic.castSpell.CastCommand(), crafting.craft.CookCommand(), system.ExitCommand(), status.status.StatusCommand(), combat.attack.AttackCommand(), quests.journal.JournalCommand(), crafting.craft.CraftRecipeCommand(), system.help.CommandsCommand(), system.RedoCommand(), traveling.routes.RouteCommand())

private val spellCommands: List<magic.spellCommands.SpellCommand> = listOf(magic.spellCommands.air.Adrenaline(), magic.spellCommands.air.Pull(), magic.spellCommands.air.Push(), magic.spellCommands.earth.Rock(), magic.spellCommands.earth.Rooted(), magic.spellCommands.water.Heal(), magic.spellCommands.fire.Flame(), magic.spellCommands.water.Poison(), magic.spellCommands.water.Jet())

private val eventParsers: List<core.events.eventParsers.EventParser> = listOf(core.events.eventParsers.SpawnActivatorEventParser(), core.events.eventParsers.MessageEventParser(), core.events.eventParsers.RemoveConditionEventParser(), core.events.eventParsers.StatChangeEventParser(), core.events.eventParsers.SetQuestStageEventParser(), core.events.eventParsers.ArriveEventParser(), core.events.eventParsers.RestrictLocationEventParser(), core.events.eventParsers.AddConditionEventParser(), core.events.eventParsers.DiscoverRecipeEventParser(), core.events.eventParsers.RemoveItemEventParser(), core.events.eventParsers.SetPropertiesEventParser(), core.events.eventParsers.SpawnItemEventParser(), core.events.eventParsers.RemoveScopeEventParser(), core.events.eventParsers.CompleteQuestEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(status.statChanged.PlayerStatMinned(), use.eat.EatFood(), system.message.DisplayMessage(), time.gameTick.TimeManager(), use.actions.ChopWood(), use.actions.UseFoodItem(), system.history.ViewChatHistory(), traveling.scope.remove.RemoveScope(), traveling.climb.AttemptClimb(), status.ExpGained(), system.debug.DebugToggleListener(), status.conditions.RemoveCondition(), system.debug.DebugListListener(), use.actions.UseOnFire(), explore.Look(), core.GameManager.MessageHandler(), use.actions.StartFire(), time.gameTick.GameTick(), combat.battle.CombatantDied(), core.history.SessionListener(), status.LevelUp(), magic.castSpell.CastSpell(), crafting.DiscoverRecipe(), status.statChanged.StatMinned(), status.statChanged.PlayerStatMaxed(), inventory.dropItem.ItemDropped(), use.interaction.nothing.StartNothing(), traveling.RestrictLocation(), crafting.craft.Craft(), traveling.routes.ViewRoute(), explore.map.ReadMap(), traveling.scope.remove.RemoveItem(), traveling.scope.spawn.ActivatorSpawner(), combat.battle.BattleTurn(), core.TurnListener(), traveling.jump.PlayerJump(), use.actions.ScratchSurface(), quests.journal.ViewQuestJournal(), status.statChanged.CreatureDied(), inventory.unEquipItem.UnEquipItem(), combat.attack.StartAttack(), combat.takeDamage.TakeDamage(), traveling.move.Move(), use.actions.UseIngredientOnActivatorRecipe(), system.debug.DebugTagListener(), quests.QuestListener(), quests.SetQuestStage(), system.help.ViewHelp(), use.interaction.nothing.DoNothing(), combat.block.Block(), core.properties.SetProperties(), system.persistance.saving.Save(), combat.battle.BattleTick(), use.interaction.NoInteractionFound(), combat.approach.StartMove(), status.statChanged.StatBoosted(), combat.block.StartBlock(), inventory.ListInventory(), combat.dodge.StartDodge(), core.properties.propValChanged.PropertyStatMinned(), traveling.scope.spawn.SpawnItem(), core.properties.propValChanged.PropertyStatChanged(), inventory.equipItem.EquipItem(), system.debug.DebugStatListener(), traveling.arrive.ArrivalHandler(), use.actions.NoUseFound(), combat.approach.Move(), core.target.item.ItemSpawner(), use.actions.DamageCreature(), inventory.dropItem.TransferItem(), traveling.travel.TravelStart(), traveling.climb.ClimbComplete(), quests.CompleteQuest(), status.status.Status(), magic.castSpell.StartCastSpell(), status.statChanged.StatChanged(), combat.dodge.Dodge(), inventory.pickupItem.ItemPickedUp(), inventory.unEquipItem.ItemUnEquipped(), quests.journal.ViewQuestList(), system.persistance.loading.Load(), status.effects.ApplyEffects(), combat.attack.Attack(), inventory.equipItem.ItemEquipped(), magic.ViewWordHelp(), traveling.routes.FindRoute(), use.actions.UseItemOnIngredientRecipe(), use.interaction.Interact(), traveling.jump.PlayerFall(), status.rest.Rest(), status.conditions.AddCondition(), traveling.arrive.Arrive(), crafting.checkRecipe.CheckRecipes())

