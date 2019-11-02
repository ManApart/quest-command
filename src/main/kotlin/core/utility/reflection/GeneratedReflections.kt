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

private val commands: List<core.commands.Command> = listOf(system.help.CommandsCommand(), inventory.InventoryCommand(), inventory.pickupItem.PickupItemCommand(), travel.RouteCommand(), status.journal.JournalCommand(), system.RedoCommand(), combat.block.BlockCommand(), crafting.CraftRecipeCommand(), explore.LookCommand(), crafting.RecipeCommand(), inventory.equipItem.EquipItemCommand(), travel.climb.ClimbCommand(), interact.interaction.nothing.NothingCommand(), system.ExitCommand(), travel.TravelCommand(), system.help.HelpCommand(), travel.jump.JumpCommand(), system.history.HistoryCommand(), explore.map.ReadMapCommand(), interact.magic.CastCommand(), system.persistance.saving.SaveCommand(), combat.approach.ApproachCommand(), interact.UseCommand(), inventory.dropItem.PlaceItemCommand(), combat.approach.RetreatCommand(), travel.TravelInDirectionCommand(), status.rest.RestCommand(), crafting.CookCommand(), interact.eat.EatCommand(), core.commands.UnknownCommand(), inventory.EquippedCommand(), combat.dodge.DodgeCommand(), combat.attack.AttackCommand(), travel.climb.DismountCommand(), inventory.unEquipItem.UnEquipItemCommand(), status.StatusCommand())

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.water.Heal(), interact.magic.spellCommands.water.Jet(), interact.magic.spellCommands.air.Push(), interact.magic.spellCommands.air.Pull())

private val eventListeners: List<core.events.EventListener<*>> = listOf(interact.actions.DamageCreature(), status.effects.AddCondition(), interact.actions.UseIngredientOnActivatorRecipe(), combat.takeDamage.TakeDamage(), combat.block.StartBlock(), interact.interaction.nothing.StartNothing(), system.history.ViewChatHistory(), interact.actions.ChopWood(), combat.attack.Attack(), travel.ViewRoute(), core.gameState.quests.CompleteQuest(), system.GameManager.MessageHandler(), inventory.pickupItem.ItemPickedUp(), interact.actions.ScratchSurface(), crafting.CheckRecipes(), interact.magic.StartCastSpell(), status.journal.ViewQuestJournal(), status.effects.ApplyEffects(), status.propValChanged.PropertyStatChanged(), status.statChanged.CreatureDied(), interact.actions.NoUseFound(), interact.scope.spawn.ActivatorSpawner(), interact.scope.remove.RemoveItem(), crafting.DiscoverRecipe(), inventory.equipItem.ItemEquipped(), combat.battle.BattleTick(), explore.map.ReadMap(), combat.approach.StartMove(), interact.scope.remove.RemoveScope(), inventory.equipItem.EquipItem(), explore.RestrictLocation(), status.statChanged.StatMinned(), interact.actions.UseOnFire(), interact.magic.ViewWordHelp(), system.persistance.saving.Save(), interact.scope.spawn.SpawnItem(), inventory.ListInventory(), travel.Arrive(), explore.Look(), interact.interaction.Interact(), interact.magic.CastSpell(), status.statChanged.StatBoosted(), status.effects.RemoveCondition(), combat.attack.StartAttack(), interact.actions.UseItemOnIngredientRecipe(), travel.FindRoute(), core.gameState.quests.SetQuestStage(), status.rest.Rest(), status.statChanged.PlayerStatMaxed(), inventory.dropItem.ItemDropped(), travel.jump.PlayerJump(), travel.TravelStart(), inventory.unEquipItem.UnEquipItem(), interact.eat.EatFood(), status.ExpGained(), interact.interaction.nothing.DoNothing(), status.LevelUp(), status.statChanged.PlayerStatMinned(), travel.climb.AttemptClimb(), combat.approach.Move(), core.history.SessionListener(), core.gameState.SetProperties(), interact.interaction.NoInteractionFound(), interact.scope.ArrivalHandler(), system.item.ItemSpawner(), combat.dodge.StartDodge(), travel.jump.PlayerFall(), combat.battle.CombatantDied(), inventory.dropItem.TransferItem(), status.Status(), system.gameTick.GameTick(), crafting.Craft(), system.help.ViewHelp(), combat.block.Block(), core.gameState.quests.QuestListener(), interact.actions.UseFoodItem(), status.journal.ViewQuestList(), combat.battle.BattleTurn(), system.gameTick.TimeManager(), status.statChanged.StatChanged(), travel.climb.ClimbComplete(), combat.dodge.Dodge(), interact.actions.StartFire(), inventory.unEquipItem.ItemUnEquipped())

