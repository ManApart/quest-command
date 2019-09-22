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

private val commands: List<core.commands.Command> = listOf(explore.map.ReadMapCommand(), system.RedoCommand(), interact.UseCommand(), crafting.RecipeCommand(), combat.dodge.DodgeCommand(), status.journal.JournalCommand(), system.history.HistoryCommand(), inventory.EquippedCommand(), inventory.pickupItem.PickupItemCommand(), system.help.HelpCommand(), inventory.dropItem.PlaceItemCommand(), inventory.unEquipItem.UnEquipItemCommand(), combat.attack.AttackCommand(), travel.climb.DismountCommand(), travel.TravelCommand(), combat.approach.ApproachCommand(), combat.approach.RetreatCommand(), crafting.CraftRecipeCommand(), combat.block.BlockCommand(), interact.eat.EatCommand(), explore.LookCommand(), travel.TravelInDirectionCommand(), travel.RouteCommand(), inventory.InventoryCommand(), system.ExitCommand(), core.commands.UnknownCommand(), interact.magic.CastCommand(), system.help.CommandsCommand(), crafting.CookCommand(), status.StatusCommand(), status.rest.RestCommand(), inventory.equipItem.EquipItemCommand(), travel.climb.ClimbCommand(), travel.jump.JumpCommand())

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.water.Jet())

private val eventListeners: List<core.events.EventListener<*>> = listOf(status.journal.ViewQuestJournal(), interact.scope.spawn.SpawnItem(), combat.dodge.StartDodge(), combat.attack.Attack(), status.rest.Rest(), status.Status(), interact.actions.UseIngredientOnActivatorRecipe(), combat.takeDamage.TakeDamage(), core.gameState.SetProperties(), travel.ViewRoute(), travel.climb.AttemptClimb(), inventory.dropItem.TransferItem(), inventory.equipItem.ItemEquipped(), inventory.unEquipItem.UnEquipItem(), combat.block.StartBlock(), crafting.Craft(), system.gameTick.TimeManager(), explore.map.ReadMap(), travel.Arrive(), interact.actions.UseOnFire(), interact.magic.CastSpell(), interact.interaction.NoInteractionFound(), interact.actions.UseFoodItem(), system.item.ItemSpawner(), system.help.ViewHelp(), status.ExpGained(), travel.jump.PlayerJump(), interact.scope.remove.RemoveScope(), combat.battle.BattleTick(), interact.scope.ArrivalHandler(), combat.approach.Move(), status.statChanged.PlayerStatMaxed(), interact.magic.ViewWordHelp(), combat.battle.CombatantDied(), combat.battle.BattleTurn(), inventory.dropItem.ItemDropped(), combat.dodge.Dodge(), system.gameTick.GameTick(), status.effects.AddCondition(), core.gameState.quests.QuestListener(), interact.scope.remove.RemoveItem(), combat.attack.StartAttack(), travel.TravelStart(), status.effects.RemoveCondition(), core.gameState.quests.CompleteQuest(), status.statChanged.PlayerStatMinned(), inventory.equipItem.EquipItem(), interact.actions.ChopWood(), crafting.DiscoverRecipe(), inventory.pickupItem.ItemPickedUp(), interact.actions.UseItemOnIngredientRecipe(), status.effects.ApplyEffects(), interact.interaction.Interact(), travel.FindRoute(), interact.eat.EatFood(), status.journal.ViewQuestList(), interact.actions.StatMinned(), crafting.CheckRecipes(), interact.scope.spawn.ActivatorSpawner(), travel.climb.ClimbComplete(), explore.RestrictLocation(), status.statChanged.StatBoosted(), combat.block.Block(), explore.Look(), status.statChanged.StatChanged(), core.gameState.quests.SetQuestStage(), inventory.ListInventory(), system.GameManager.MessageHandler(), interact.magic.StartCastSpell(), interact.actions.StartFire(), system.history.ViewChatHistory(), interact.actions.ScratchSurface(), status.LevelUp(), combat.approach.StartMove(), status.statChanged.CreatureDied(), interact.actions.NoUseFound(), interact.actions.DamageCreature(), travel.jump.PlayerFall(), inventory.unEquipItem.ItemUnEquipped())

