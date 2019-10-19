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

private val commands: List<core.commands.Command> = listOf(travel.climb.DismountCommand(), inventory.unEquipItem.UnEquipItemCommand(), system.RedoCommand(), status.rest.RestCommand(), interact.UseCommand(), core.commands.UnknownCommand(), system.help.HelpCommand(), system.ExitCommand(), explore.LookCommand(), inventory.pickupItem.PickupItemCommand(), combat.dodge.DodgeCommand(), travel.climb.ClimbCommand(), travel.RouteCommand(), travel.TravelCommand(), inventory.dropItem.PlaceItemCommand(), travel.TravelInDirectionCommand(), crafting.CookCommand(), status.journal.JournalCommand(), interact.eat.EatCommand(), interact.interaction.nothing.NothingCommand(), explore.map.ReadMapCommand(), inventory.EquippedCommand(), status.StatusCommand(), interact.magic.CastCommand(), system.help.CommandsCommand(), combat.approach.RetreatCommand(), combat.block.BlockCommand(), system.persistance.saving.SaveCommand(), combat.approach.ApproachCommand(), crafting.CraftRecipeCommand(), travel.jump.JumpCommand(), combat.attack.AttackCommand(), system.history.HistoryCommand(), inventory.equipItem.EquipItemCommand(), crafting.RecipeCommand(), inventory.InventoryCommand())

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.water.Jet())

private val eventListeners: List<core.events.EventListener<*>> = listOf(interact.scope.spawn.ActivatorSpawner(), travel.jump.PlayerJump(), system.persistance.saving.Save(), inventory.ListInventory(), status.ExpGained(), inventory.dropItem.TransferItem(), combat.dodge.StartDodge(), travel.TravelStart(), interact.actions.UseIngredientOnActivatorRecipe(), status.statChanged.PlayerStatMaxed(), explore.Look(), interact.magic.CastSpell(), status.rest.Rest(), system.history.ViewChatHistory(), crafting.DiscoverRecipe(), interact.actions.UseOnFire(), crafting.CheckRecipes(), core.gameState.quests.SetQuestStage(), inventory.pickupItem.ItemPickedUp(), status.statChanged.StatChanged(), inventory.dropItem.ItemDropped(), interact.scope.remove.RemoveItem(), combat.block.Block(), interact.interaction.NoInteractionFound(), system.gameTick.GameTick(), interact.actions.NoUseFound(), inventory.unEquipItem.ItemUnEquipped(), status.effects.RemoveCondition(), core.history.SessionListener(), core.gameState.quests.CompleteQuest(), combat.dodge.Dodge(), interact.actions.StatMinned(), interact.actions.ChopWood(), interact.scope.remove.RemoveScope(), interact.magic.StartCastSpell(), combat.takeDamage.TakeDamage(), explore.RestrictLocation(), interact.scope.spawn.SpawnItem(), status.Status(), travel.climb.ClimbComplete(), status.statChanged.StatBoosted(), core.gameState.SetProperties(), core.gameState.quests.QuestListener(), status.LevelUp(), interact.actions.UseFoodItem(), travel.ViewRoute(), travel.FindRoute(), interact.scope.ArrivalHandler(), combat.battle.BattleTurn(), status.statChanged.CreatureDied(), crafting.Craft(), interact.eat.EatFood(), interact.actions.DamageCreature(), interact.actions.UseItemOnIngredientRecipe(), combat.attack.StartAttack(), combat.battle.CombatantDied(), combat.approach.StartMove(), system.GameManager.MessageHandler(), interact.actions.StartFire(), combat.approach.Move(), system.item.ItemSpawner(), inventory.equipItem.ItemEquipped(), status.journal.ViewQuestList(), travel.jump.PlayerFall(), combat.block.StartBlock(), interact.interaction.nothing.DoNothing(), interact.actions.ScratchSurface(), status.statChanged.PlayerStatMinned(), status.effects.ApplyEffects(), interact.magic.ViewWordHelp(), system.gameTick.TimeManager(), status.effects.AddCondition(), combat.attack.Attack(), travel.Arrive(), inventory.unEquipItem.UnEquipItem(), interact.interaction.Interact(), status.journal.ViewQuestJournal(), combat.battle.BattleTick(), explore.map.ReadMap(), interact.interaction.nothing.StartNothing(), inventory.equipItem.EquipItem(), travel.climb.AttemptClimb(), system.help.ViewHelp())

