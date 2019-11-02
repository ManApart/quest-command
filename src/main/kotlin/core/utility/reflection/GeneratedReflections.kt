package core.utility.reflection

import core.commands.Command
import core.events.EventListener
import interact.magic.spellCommands.SpellCommand

class GeneratedReflections : Reflections {
    override fun getCommands(): List<Command> {
        return commands
    }

    override fun getSpellCommands(): List<SpellCommand> {
        return spellCommands
    }

    override fun getEventListeners(): List<EventListener<*>> {
        return eventListeners
    }
}

private val commands: List<core.commands.Command> = listOf(system.RedoCommand(), crafting.CookCommand(), system.persistance.saving.SaveCommand(), inventory.unEquipItem.UnEquipItemCommand(), inventory.InventoryCommand(), travel.RouteCommand(), system.ExitCommand(), system.help.CommandsCommand(), inventory.EquippedCommand(), travel.climb.ClimbCommand(), core.commands.UnknownCommand(), inventory.dropItem.PlaceItemCommand(), combat.block.BlockCommand(), system.history.HistoryCommand(), combat.attack.AttackCommand(), combat.approach.RetreatCommand(), combat.approach.ApproachCommand(), status.rest.RestCommand(), interact.interaction.nothing.NothingCommand(), combat.dodge.DodgeCommand(), travel.TravelInDirectionCommand(), inventory.pickupItem.PickupItemCommand(), crafting.RecipeCommand(), travel.climb.DismountCommand(), travel.jump.JumpCommand(), travel.TravelCommand(), inventory.equipItem.EquipItemCommand(), interact.UseCommand(), interact.magic.CastCommand(), crafting.CraftRecipeCommand(), system.help.HelpCommand(), interact.eat.EatCommand(), status.journal.JournalCommand(), status.StatusCommand(), explore.map.ReadMapCommand(), explore.LookCommand())

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.air.Push(), interact.magic.spellCommands.water.Jet(), interact.magic.spellCommands.air.Pull(), interact.magic.spellCommands.water.Heal())

private val eventListeners: List<core.events.EventListener<*>> = listOf(status.statChanged.StatBoosted(), inventory.unEquipItem.UnEquipItem(), combat.block.StartBlock(), travel.climb.ClimbComplete(), interact.interaction.nothing.DoNothing(), combat.block.Block(), combat.dodge.Dodge(), interact.scope.remove.RemoveItem(), status.Status(), inventory.unEquipItem.ItemUnEquipped(), explore.map.ReadMap(), interact.actions.UseIngredientOnActivatorRecipe(), interact.scope.ArrivalHandler(), status.effects.ApplyEffects(), travel.jump.PlayerFall(), interact.scope.remove.RemoveScope(), inventory.ListInventory(), status.effects.AddCondition(), status.statChanged.PlayerStatMinned(), travel.Arrive(), status.statChanged.StatMinned(), core.gameState.SetProperties(), interact.interaction.nothing.StartNothing(), interact.eat.EatFood(), system.item.ItemSpawner(), system.gameTick.GameTick(), interact.magic.CastSpell(), interact.actions.DamageCreature(), interact.actions.ScratchSurface(), combat.attack.Attack(), inventory.equipItem.EquipItem(), combat.approach.Move(), system.GameManager.MessageHandler(), core.gameState.quests.QuestListener(), interact.actions.StartFire(), travel.climb.AttemptClimb(), combat.dodge.StartDodge(), combat.approach.StartMove(), system.history.ViewChatHistory(), status.effects.RemoveCondition(), combat.battle.BattleTick(), travel.TravelStart(), crafting.DiscoverRecipe(), combat.attack.StartAttack(), crafting.Craft(), interact.interaction.NoInteractionFound(), inventory.dropItem.ItemDropped(), interact.magic.ViewWordHelp(), status.LevelUp(), inventory.pickupItem.ItemPickedUp(), explore.Look(), status.journal.ViewQuestJournal(), interact.actions.UseFoodItem(), status.ExpGained(), status.journal.ViewQuestList(), inventory.dropItem.TransferItem(), interact.actions.UseOnFire(), interact.scope.spawn.ActivatorSpawner(), combat.battle.BattleTurn(), system.gameTick.TimeManager(), interact.magic.StartCastSpell(), interact.actions.ChopWood(), status.statChanged.CreatureDied(), interact.scope.spawn.SpawnItem(), travel.FindRoute(), inventory.equipItem.ItemEquipped(), crafting.CheckRecipes(), interact.actions.NoUseFound(), core.history.SessionListener(), core.gameState.quests.SetQuestStage(), system.persistance.saving.Save(), core.gameState.quests.CompleteQuest(), status.rest.Rest(), status.propValChanged.PropertyStatChanged(), status.statChanged.PlayerStatMaxed(), combat.battle.CombatantDied(), system.help.ViewHelp(), status.statChanged.StatChanged(), interact.actions.UseItemOnIngredientRecipe(), interact.interaction.Interact(), explore.RestrictLocation(), travel.jump.PlayerJump(), travel.ViewRoute(), combat.takeDamage.TakeDamage())

