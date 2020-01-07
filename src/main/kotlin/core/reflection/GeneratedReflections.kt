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

private val commands: List<core.commands.Command> = listOf(system.history.HistoryCommand(), combat.dodge.DodgeCommand(), use.eat.EatCommand(), traveling.travel.TravelCommand(), system.persistance.saving.SaveCommand(), status.rest.RestCommand(), inventory.InventoryCommand(), system.ExitCommand(), combat.block.BlockCommand(), system.persistance.loading.LoadCommand(), inventory.pickupItem.PickupItemCommand(), traveling.climb.ClimbCommand(), crafting.checkRecipe.RecipeCommand(), crafting.craft.CraftRecipeCommand(), combat.approach.RetreatCommand(), quests.journal.JournalCommand(), system.help.HelpCommand(), traveling.move.MoveCommand(), system.help.CommandsCommand(), use.UseCommand(), traveling.travel.TravelInDirectionCommand(), inventory.equipItem.EquipItemCommand(), crafting.craft.CookCommand(), inventory.unEquipItem.UnEquipItemCommand(), combat.attack.AttackCommand(), explore.LookCommand(), core.commands.UnknownCommand(), traveling.jump.JumpCommand(), system.RedoCommand(), explore.map.ReadMapCommand(), system.debug.DebugCommand(), traveling.climb.DismountCommand(), use.interaction.nothing.NothingCommand(), status.status.StatusCommand(), magic.castSpell.CastCommand(), system.persistance.rename.RenameCommand(), combat.approach.ApproachCommand(), inventory.EquippedCommand(), inventory.dropItem.PlaceItemCommand(), traveling.routes.RouteCommand())

private val spellCommands: List<magic.spellCommands.SpellCommand> = listOf(magic.spellCommands.fire.Flame(), magic.spellCommands.earth.Rooted(), magic.spellCommands.air.Push(), magic.spellCommands.water.Heal(), magic.spellCommands.air.Pull(), magic.spellCommands.water.Jet(), magic.spellCommands.air.Adrenaline(), magic.spellCommands.water.Poison(), magic.spellCommands.earth.Rock())

private val eventParsers: List<core.events.eventParsers.EventParser> = listOf(core.events.eventParsers.DiscoverRecipeEventParser(), core.events.eventParsers.RemoveScopeEventParser(), core.events.eventParsers.SpawnItemEventParser(), core.events.eventParsers.ArriveEventParser(), core.events.eventParsers.RemoveConditionEventParser(), core.events.eventParsers.MessageEventParser(), core.events.eventParsers.AddConditionEventParser(), core.events.eventParsers.CompleteQuestEventParser(), core.events.eventParsers.RestrictLocationEventParser(), core.events.eventParsers.StatChangeEventParser(), core.events.eventParsers.RemoveItemEventParser(), core.events.eventParsers.SetQuestStageEventParser(), core.events.eventParsers.SetPropertiesEventParser(), core.events.eventParsers.SpawnActivatorEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(system.persistance.loading.ListSaves(), traveling.climb.AttemptClimb(), quests.journal.ViewQuestList(), inventory.unEquipItem.ItemUnEquipped(), inventory.dropItem.TransferItem(), time.gameTick.TimeManager(), status.statChanged.StatChanged(), status.effects.ApplyEffects(), use.actions.UseIngredientOnActivatorRecipe(), traveling.scope.remove.RemoveItem(), use.interaction.nothing.DoNothing(), core.properties.propValChanged.PropertyStatMinned(), status.statChanged.PlayerStatMaxed(), use.actions.UseItemOnIngredientRecipe(), inventory.dropItem.ItemDropped(), inventory.ListInventory(), use.actions.DamageCreature(), inventory.pickupItem.ItemPickedUp(), inventory.equipItem.EquipItem(), combat.battle.BattleTurn(), combat.block.StartBlock(), use.actions.UseFoodItem(), combat.block.Block(), system.debug.DebugTagListener(), combat.takeDamage.TakeDamage(), quests.CompleteQuest(), use.interaction.Interact(), inventory.unEquipItem.UnEquipItem(), status.rest.Rest(), combat.battle.BattleTick(), use.eat.EatFood(), use.actions.StartFire(), combat.dodge.Dodge(), crafting.DiscoverRecipe(), traveling.jump.PlayerJump(), system.help.ViewHelp(), use.actions.UseOnFire(), traveling.arrive.ArrivalHandler(), quests.QuestListener(), quests.journal.ViewQuestJournal(), traveling.RestrictLocation(), magic.ViewWordHelp(), combat.attack.Attack(), crafting.checkRecipe.CheckRecipes(), crafting.craft.Craft(), use.actions.NoUseFound(), status.conditions.AddCondition(), status.LevelUp(), system.persistance.rename.Rename(), status.statChanged.CreatureDied(), status.status.Status(), status.statChanged.StatMinned(), system.persistance.loading.Load(), status.conditions.RemoveCondition(), magic.castSpell.CastSpell(), core.properties.SetProperties(), magic.castSpell.StartCastSpell(), system.persistance.saving.Save(), system.debug.DebugStatListener(), explore.Look(), core.TurnListener(), traveling.climb.ClimbComplete(), combat.approach.StartMove(), explore.map.ReadMap(), use.interaction.nothing.StartNothing(), status.statChanged.PlayerStatMinned(), status.statChanged.StatBoosted(), use.actions.ChopWood(), traveling.jump.PlayerFall(), use.interaction.NoInteractionFound(), traveling.arrive.Arrive(), combat.battle.CombatantDied(), system.history.ViewChatHistory(), traveling.scope.spawn.SpawnItem(), combat.dodge.StartDodge(), traveling.scope.spawn.ActivatorSpawner(), quests.SetQuestStage(), inventory.equipItem.ItemEquipped(), traveling.routes.ViewRoute(), traveling.travel.TravelStart(), time.gameTick.GameTick(), traveling.routes.FindRoute(), core.properties.propValChanged.PropertyStatChanged(), traveling.move.Move(), traveling.scope.remove.RemoveScope(), core.GameManager.MessageHandler(), combat.approach.Move(), core.target.item.ItemSpawner(), combat.attack.StartAttack(), use.actions.ScratchSurface(), core.history.SessionListener(), system.debug.DebugToggleListener(), system.message.DisplayMessage(), status.ExpGained(), system.debug.DebugListListener())

