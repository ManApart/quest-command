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

private val commands: List<core.commands.Command> = listOf(system.history.HistoryCommand(), system.help.HelpCommand(), use.eat.EatCommand(), traveling.travel.TravelCommand(), core.commands.UnknownCommand(), status.rest.RestCommand(), inventory.InventoryCommand(), system.ExitCommand(), combat.block.BlockCommand(), system.persistance.loading.LoadCommand(), inventory.pickupItem.PickupItemCommand(), traveling.climb.ClimbCommand(), crafting.checkRecipe.RecipeCommand(), crafting.craft.CraftRecipeCommand(), combat.approach.RetreatCommand(), system.persistance.saving.SaveCommand(), traveling.climb.DismountCommand(), traveling.move.MoveCommand(), system.help.CommandsCommand(), use.UseCommand(), traveling.travel.TravelInDirectionCommand(), inventory.equipItem.EquipItemCommand(), system.persistance.rename.RenameCommand(), inventory.unEquipItem.UnEquipItemCommand(), combat.attack.AttackCommand(), explore.LookCommand(), combat.dodge.DodgeCommand(), traveling.jump.JumpCommand(), system.RedoCommand(), explore.map.ReadMapCommand(), system.debug.DebugCommand(), crafting.craft.CookCommand(), use.interaction.nothing.NothingCommand(), status.status.StatusCommand(), magic.castSpell.CastCommand(), quests.journal.JournalCommand(), combat.approach.ApproachCommand(), inventory.EquippedCommand(), inventory.dropItem.PlaceItemCommand(), traveling.routes.RouteCommand())

private val spellCommands: List<magic.spellCommands.SpellCommand> = listOf(magic.spellCommands.fire.Flame(), magic.spellCommands.earth.Rooted(), magic.spellCommands.air.Push(), magic.spellCommands.water.Heal(), magic.spellCommands.air.Pull(), magic.spellCommands.water.Jet(), magic.spellCommands.air.Adrenaline(), magic.spellCommands.water.Poison(), magic.spellCommands.earth.Rock())

private val eventParsers: List<core.events.eventParsers.EventParser> = listOf(core.events.eventParsers.DiscoverRecipeEventParser(), core.events.eventParsers.RemoveScopeEventParser(), core.events.eventParsers.SpawnItemEventParser(), core.events.eventParsers.ArriveEventParser(), core.events.eventParsers.RemoveConditionEventParser(), core.events.eventParsers.MessageEventParser(), core.events.eventParsers.AddConditionEventParser(), core.events.eventParsers.CompleteQuestEventParser(), core.events.eventParsers.RestrictLocationEventParser(), core.events.eventParsers.StatChangeEventParser(), core.events.eventParsers.RemoveItemEventParser(), core.events.eventParsers.SetQuestStageEventParser(), core.events.eventParsers.SetPropertiesEventParser(), core.events.eventParsers.SpawnActivatorEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(quests.journal.ViewQuestJournal(), traveling.RestrictLocation(), status.LevelUp(), traveling.jump.PlayerJump(), explore.map.ReadMap(), use.actions.UseIngredientOnActivatorRecipe(), system.debug.DebugListListener(), traveling.arrive.Arrive(), system.message.DisplayMessage(), use.interaction.nothing.DoNothing(), inventory.dropItem.TransferItem(), traveling.travel.TravelStart(), use.actions.ChopWood(), use.actions.UseFoodItem(), inventory.unEquipItem.UnEquipItem(), traveling.routes.ViewRoute(), use.actions.ScratchSurface(), status.conditions.AddCondition(), quests.journal.ViewQuestList(), combat.attack.StartAttack(), core.properties.propValChanged.PropertyStatChanged(), combat.takeDamage.TakeDamage(), status.statChanged.CreatureDied(), status.status.Status(), core.GameManager.MessageHandler(), combat.attack.Attack(), use.actions.UseOnFire(), status.statChanged.StatMinned(), system.persistance.loading.Load(), status.statChanged.StatChanged(), use.interaction.NoInteractionFound(), time.gameTick.TimeManager(), explore.Look(), core.properties.SetProperties(), core.history.SessionListener(), system.help.ViewHelp(), status.statChanged.PlayerStatMaxed(), system.history.ViewChatHistory(), traveling.jump.PlayerFall(), combat.dodge.StartDodge(), inventory.unEquipItem.ItemUnEquipped(), traveling.climb.ClimbComplete(), system.persistance.saving.Save(), status.conditions.RemoveCondition(), use.interaction.nothing.StartNothing(), inventory.equipItem.EquipItem(), use.interaction.Interact(), core.TurnListener(), use.actions.StartFire(), status.statChanged.PlayerStatMinned(), combat.block.Block(), crafting.craft.Craft(), status.statChanged.StatBoosted(), status.ExpGained(), system.debug.DebugTagListener(), quests.CompleteQuest(), inventory.pickupItem.ItemPickedUp(), status.effects.ApplyEffects(), inventory.ListInventory(), status.rest.Rest(), combat.battle.BattleTick(), use.eat.EatFood(), traveling.arrive.ArrivalHandler(), traveling.climb.AttemptClimb(), crafting.checkRecipe.CheckRecipes(), traveling.scope.spawn.SpawnItem(), use.actions.UseItemOnIngredientRecipe(), combat.dodge.Dodge(), traveling.scope.remove.RemoveItem(), crafting.DiscoverRecipe(), inventory.dropItem.ItemDropped(), traveling.scope.spawn.ActivatorSpawner(), quests.SetQuestStage(), inventory.equipItem.ItemEquipped(), core.properties.propValChanged.PropertyStatMinned(), system.persistance.rename.Rename(), time.gameTick.GameTick(), combat.battle.BattleTurn(), combat.block.StartBlock(), magic.ViewWordHelp(), traveling.move.Move(), system.debug.DebugStatListener(), magic.castSpell.StartCastSpell(), combat.approach.Move(), core.target.item.ItemSpawner(), use.actions.DamageCreature(), use.actions.NoUseFound(), traveling.routes.FindRoute(), traveling.scope.remove.RemoveScope(), system.debug.DebugToggleListener(), quests.QuestListener(), combat.battle.CombatantDied(), magic.castSpell.CastSpell(), combat.approach.StartMove())

