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

private val eventListeners: List<core.events.EventListener<*>> = listOf(interact.scope.ArrivalHandler(), status.effects.AddCondition(), interact.actions.UseIngredientOnActivatorRecipe(), combat.takeDamage.TakeDamage(), combat.block.StartBlock(), combat.block.Block(), system.history.ViewChatHistory(), status.effects.RemoveCondition(), combat.attack.Attack(), travel.ViewRoute(), core.gameState.quests.CompleteQuest(), system.GameManager.MessageHandler(), inventory.pickupItem.ItemPickedUp(), interact.actions.DamageCreature(), crafting.CheckRecipes(), travel.jump.PlayerFall(), interact.scope.remove.RemoveScope(), status.effects.ApplyEffects(), status.propValChanged.PropertyStatChanged(), status.statChanged.PlayerStatMinned(), travel.Arrive(), status.statChanged.StatMinned(), interact.scope.remove.RemoveItem(), interact.interaction.nothing.StartNothing(), interact.eat.EatFood(), travel.jump.PlayerJump(), explore.map.ReadMap(), combat.approach.StartMove(), system.TurnListener(), interact.actions.ScratchSurface(), explore.RestrictLocation(), inventory.equipItem.EquipItem(), combat.approach.Move(), interact.magic.ViewWordHelp(), system.persistance.saving.Save(), system.message.DisplayMessage(), inventory.ListInventory(), travel.climb.AttemptClimb(), explore.Look(), interact.interaction.Interact(), interact.magic.CastSpell(), interact.actions.ChopWood(), combat.dodge.Dodge(), combat.attack.StartAttack(), crafting.DiscoverRecipe(), travel.FindRoute(), core.gameState.quests.SetQuestStage(), interact.interaction.NoInteractionFound(), status.statChanged.PlayerStatMaxed(), inventory.dropItem.ItemDropped(), system.item.ItemSpawner(), travel.TravelStart(), inventory.unEquipItem.UnEquipItem(), status.journal.ViewQuestJournal(), status.ExpGained(), interact.interaction.nothing.DoNothing(), interact.scope.spawn.SpawnItem(), inventory.dropItem.TransferItem(), interact.actions.UseOnFire(), interact.scope.spawn.ActivatorSpawner(), core.history.SessionListener(), core.gameState.SetProperties(), interact.magic.StartCastSpell(), status.statChanged.StatBoosted(), status.statChanged.CreatureDied(), status.LevelUp(), combat.dodge.StartDodge(), inventory.equipItem.ItemEquipped(), combat.battle.CombatantDied(), interact.actions.NoUseFound(), status.Status(), system.gameTick.GameTick(), crafting.Craft(), system.help.ViewHelp(), status.rest.Rest(), core.gameState.quests.QuestListener(), interact.actions.UseFoodItem(), status.journal.ViewQuestList(), combat.battle.BattleTurn(), system.gameTick.TimeManager(), interact.actions.UseItemOnIngredientRecipe(), status.statChanged.StatChanged(), travel.climb.ClimbComplete(), combat.battle.BattleTick(), interact.actions.StartFire(), inventory.unEquipItem.ItemUnEquipped())

