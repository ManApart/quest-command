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

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.air.Push(), interact.magic.spellCommands.water.Jet(), interact.magic.spellCommands.air.Pull())

private val eventListeners: List<core.events.EventListener<*>> = listOf(interact.actions.ChopWood(), explore.Look(), interact.interaction.nothing.DoNothing(), explore.RestrictLocation(), status.ExpGained(), interact.actions.NoUseFound(), combat.battle.BattleTick(), core.gameState.SetProperties(), core.history.SessionListener(), combat.takeDamage.TakeDamage(), system.gameTick.GameTick(), combat.block.StartBlock(), status.statChanged.StatBoosted(), inventory.ListInventory(), combat.approach.Move(), interact.actions.ScratchSurface(), interact.actions.StartFire(), inventory.unEquipItem.UnEquipItem(), interact.interaction.nothing.StartNothing(), interact.interaction.NoInteractionFound(), status.journal.ViewQuestJournal(), system.gameTick.TimeManager(), inventory.dropItem.TransferItem(), interact.actions.StatMinned(), status.LevelUp(), core.gameState.quests.SetQuestStage(), system.history.ViewChatHistory(), interact.scope.ArrivalHandler(), interact.actions.DamageCreature(), interact.scope.remove.RemoveItem(), interact.scope.remove.RemoveScope(), inventory.equipItem.ItemEquipped(), explore.map.ReadMap(), combat.battle.CombatantDied(), travel.ViewRoute(), interact.magic.StartCastSpell(), travel.FindRoute(), interact.magic.CastSpell(), combat.dodge.Dodge(), travel.jump.PlayerJump(), inventory.pickupItem.ItemPickedUp(), status.statChanged.PlayerStatMinned(), travel.TravelStart(), system.persistance.saving.Save(), travel.climb.AttemptClimb(), interact.magic.ViewWordHelp(), system.GameManager.MessageHandler(), interact.scope.spawn.SpawnItem(), interact.actions.UseIngredientOnActivatorRecipe(), combat.dodge.StartDodge(), inventory.equipItem.EquipItem(), status.statChanged.PlayerStatMaxed(), interact.actions.UseFoodItem(), core.gameState.quests.QuestListener(), combat.block.Block(), travel.jump.PlayerFall(), interact.eat.EatFood(), system.help.ViewHelp(), status.statChanged.StatChanged(), interact.actions.UseOnFire(), status.effects.RemoveCondition(), crafting.DiscoverRecipe(), status.journal.ViewQuestList(), combat.attack.StartAttack(), interact.scope.spawn.ActivatorSpawner(), status.effects.ApplyEffects(), status.rest.Rest(), interact.actions.UseItemOnIngredientRecipe(), combat.battle.BattleTurn(), crafting.Craft(), inventory.unEquipItem.ItemUnEquipped(), travel.Arrive(), status.effects.AddCondition(), inventory.dropItem.ItemDropped(), crafting.CheckRecipes(), core.gameState.quests.CompleteQuest(), interact.interaction.Interact(), status.statChanged.CreatureDied(), combat.approach.StartMove(), combat.attack.Attack(), system.item.ItemSpawner(), status.Status(), travel.climb.ClimbComplete())

