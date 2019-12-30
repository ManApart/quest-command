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

private val commands: List<core.commands.Command> = listOf(explore.LookCommand(), system.help.HelpCommand(), use.eat.EatCommand(), system.help.CommandsCommand(), core.commands.UnknownCommand(), inventory.EquippedCommand(), combat.approach.ApproachCommand(), traveling.climb.ClimbCommand(), system.debug.DebugCommand(), system.ExitCommand(), inventory.pickupItem.PickupItemCommand(), system.history.HistoryCommand(), combat.approach.RetreatCommand(), traveling.move.MoveCommand(), inventory.InventoryCommand(), system.persistance.saving.SaveCommand(), traveling.climb.DismountCommand(), combat.block.BlockCommand(), inventory.dropItem.PlaceItemCommand(), traveling.jump.JumpCommand(), system.RedoCommand(), inventory.equipItem.EquipItemCommand(), use.UseCommand(), use.interaction.nothing.NothingCommand(), status.rest.RestCommand(), combat.dodge.DodgeCommand(), crafting.checkRecipe.RecipeCommand(), explore.map.ReadMapCommand(), inventory.unEquipItem.UnEquipItemCommand(), magic.castSpell.CastCommand(), crafting.craft.CookCommand(), system.persistance.loading.LoadCommand(), status.status.StatusCommand(), combat.attack.AttackCommand(), quests.journal.JournalCommand(), crafting.craft.CraftRecipeCommand(), traveling.travel.TravelCommand(), traveling.travel.TravelInDirectionCommand(), traveling.routes.RouteCommand())

private val spellCommands: List<magic.spellCommands.SpellCommand> = listOf(magic.spellCommands.water.Poison(), magic.spellCommands.water.Heal(), magic.spellCommands.earth.Rock(), magic.spellCommands.air.Push(), magic.spellCommands.water.Jet(), magic.spellCommands.earth.Rooted(), magic.spellCommands.air.Adrenaline(), magic.spellCommands.fire.Flame(), magic.spellCommands.air.Pull())

private val eventParsers: List<core.events.eventParsers.EventParser> = listOf(core.events.eventParsers.RemoveConditionEventParser(), core.events.eventParsers.DiscoverRecipeEventParser(), core.events.eventParsers.SpawnActivatorEventParser(), core.events.eventParsers.SpawnItemEventParser(), core.events.eventParsers.MessageEventParser(), core.events.eventParsers.SetQuestStageEventParser(), core.events.eventParsers.SetPropertiesEventParser(), core.events.eventParsers.RemoveItemEventParser(), core.events.eventParsers.RemoveScopeEventParser(), core.events.eventParsers.CompleteQuestEventParser(), core.events.eventParsers.AddConditionEventParser(), core.events.eventParsers.ArriveEventParser(), core.events.eventParsers.RestrictLocationEventParser(), core.events.eventParsers.StatChangeEventParser())

private val eventListeners: List<core.events.EventListener<*>> = listOf(crafting.DiscoverRecipe(), status.statChanged.StatMinned(), quests.journal.ViewQuestList(), inventory.dropItem.ItemDropped(), status.statChanged.StatChanged(), use.actions.NoUseFound(), system.persistance.saving.Save(), traveling.arrive.ArrivalHandler(), combat.battle.CombatantDied(), combat.takeDamage.TakeDamage(), combat.block.Block(), explore.map.ReadMap(), crafting.craft.Craft(), use.actions.UseItemOnIngredientRecipe(), inventory.ListInventory(), core.properties.propValChanged.PropertyStatMinned(), use.actions.UseFoodItem(), quests.CompleteQuest(), system.message.DisplayMessage(), use.actions.DamageCreature(), magic.ViewWordHelp(), status.ExpGained(), quests.journal.ViewQuestJournal(), traveling.RestrictLocation(), magic.castSpell.StartCastSpell(), traveling.jump.PlayerJump(), use.eat.EatFood(), status.status.Status(), system.debug.DebugTagListener(), traveling.travel.TravelStart(), combat.dodge.StartDodge(), status.rest.Rest(), traveling.routes.FindRoute(), combat.block.StartBlock(), combat.attack.Attack(), use.actions.UseIngredientOnActivatorRecipe(), status.statChanged.CreatureDied(), status.effects.ApplyEffects(), traveling.scope.remove.RemoveItem(), use.interaction.nothing.DoNothing(), combat.approach.Move(), crafting.checkRecipe.CheckRecipes(), use.actions.ChopWood(), inventory.equipItem.EquipItem(), quests.QuestListener(), inventory.pickupItem.ItemPickedUp(), inventory.unEquipItem.ItemUnEquipped(), use.actions.UseOnFire(), inventory.dropItem.TransferItem(), system.debug.DebugToggleListener(), combat.approach.StartMove(), combat.dodge.Dodge(), magic.castSpell.CastSpell(), system.debug.DebugListListener(), time.gameTick.GameTick(), system.help.ViewHelp(), inventory.unEquipItem.UnEquipItem(), explore.Look(), traveling.routes.ViewRoute(), traveling.scope.spawn.ActivatorSpawner(), core.target.item.ItemSpawner(), use.interaction.nothing.StartNothing(), traveling.scope.remove.RemoveScope(), traveling.arrive.Arrive(), use.actions.ScratchSurface(), traveling.climb.ClimbComplete(), combat.battle.BattleTick(), combat.attack.StartAttack(), status.statChanged.PlayerStatMinned(), time.gameTick.TimeManager(), traveling.scope.spawn.SpawnItem(), status.conditions.AddCondition(), system.persistance.loading.Load(), core.GameManager.MessageHandler(), status.conditions.RemoveCondition(), status.statChanged.StatBoosted(), use.interaction.Interact(), core.TurnListener(), status.LevelUp(), inventory.equipItem.ItemEquipped(), traveling.jump.PlayerFall(), status.statChanged.PlayerStatMaxed(), traveling.move.Move(), core.properties.propValChanged.PropertyStatChanged(), core.history.SessionListener(), use.actions.StartFire(), combat.battle.BattleTurn(), system.debug.DebugStatListener(), core.properties.SetProperties(), quests.SetQuestStage(), traveling.climb.AttemptClimb(), use.interaction.NoInteractionFound(), system.history.ViewChatHistory())

