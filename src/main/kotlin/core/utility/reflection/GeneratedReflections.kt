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

private val commands: List<core.commands.Command> = listOf(inventory.InventoryCommand(), travel.TravelInDirectionCommand(), inventory.EquippedCommand(), interact.eat.EatCommand(), status.StatusCommand(), explore.LookCommand(), interact.UseCommand(), system.ExitCommand(), inventory.unEquipItem.UnEquipItemCommand(), system.RedoCommand(), interact.magic.CastCommand(), core.commands.UnknownCommand(), inventory.dropItem.PlaceItemCommand(), crafting.RecipeCommand(), explore.map.ReadMapCommand(), combat.approach.RetreatCommand(), combat.block.BlockCommand(), inventory.pickupItem.PickupItemCommand(), status.journal.JournalCommand(), travel.TravelCommand(), travel.jump.JumpCommand(), travel.climb.DismountCommand(), crafting.CraftRecipeCommand(), inventory.equipItem.EquipItemCommand(), system.help.HelpCommand(), status.rest.RestCommand(), travel.RouteCommand(), system.help.CommandsCommand(), combat.approach.AproachCommand(), crafting.CookCommand(), travel.climb.ClimbCommand(), combat.attack.AttackCommand(), system.history.HistoryCommand(), combat.dodge.DodgeCommand())

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.water.Jet())

private val eventListeners: List<core.events.EventListener<*>> = listOf(combat.battle.CombatantDied(), combat.battle.BattleTurn(), explore.RestrictLocation(), travel.jump.PlayerFall(), interact.actions.ChopWood(), inventory.dropItem.ItemDropped(), explore.map.ReadMap(), core.gameState.quests.SetQuestStage(), interact.actions.NoUseFound(), status.statChanged.PlayerStatMaxed(), combat.dodge.Dodge(), interact.eat.EatFood(), interact.actions.DamageCreature(), interact.scope.spawn.SpawnItem(), combat.takeDamage.TakeDamage(), system.gameTick.GameTick(), crafting.DiscoverRecipe(), combat.block.Block(), inventory.dropItem.TransferItem(), status.rest.Rest(), combat.approach.Approach(), system.item.ItemSpawner(), status.effects.RemoveCondition(), system.history.ViewChatHistory(), status.Status(), system.gameTick.TimeManager(), crafting.Craft(), combat.attack.StartAttack(), status.effects.AddCondition(), travel.FindRoute(), inventory.ListInventory(), status.LevelUp(), interact.actions.UseItemOnIngredientRecipe(), interact.actions.UseIngredientOnActivatorRecipe(), core.gameState.SetProperties(), travel.ViewRoute(), combat.block.StartBlock(), system.help.ViewHelp(), interact.scope.remove.RemoveItem(), interact.magic.ViewWordHelp(), combat.dodge.StartDodge(), status.statChanged.StatChanged(), interact.actions.ScratchSurface(), interact.scope.spawn.ActivatorSpawner(), crafting.CheckRecipes(), interact.actions.StartFire(), interact.scope.remove.RemoveScope(), system.GameManager.MessageHandler(), status.journal.ViewQuestJournal(), inventory.unEquipItem.ItemUnEquipped(), combat.attack.Attack(), status.journal.ViewQuestList(), inventory.pickupItem.ItemPickedUp(), interact.actions.UseOnFire(), core.gameState.quests.QuestListener(), core.gameState.quests.CompleteQuest(), combat.approach.StartApproach(), travel.climb.AttemptClimb(), interact.magic.CastSpell(), explore.Look(), inventory.equipItem.ItemEquipped(), interact.scope.ArrivalHandler(), travel.climb.ClimbComplete(), status.statChanged.CreatureDied(), status.ExpGained(), interact.actions.StatMinned(), combat.battle.BattleTick(), interact.actions.UseFoodItem(), status.effects.ApplyEffects(), travel.TravelStart(), status.statChanged.PlayerStatMinned(), interact.interaction.NoInteractionFound(), interact.magic.StartCastSpell(), travel.Arrive(), inventory.equipItem.EquipItem(), status.statChanged.StatBoosted(), inventory.unEquipItem.UnEquipItem(), travel.jump.PlayerJump(), interact.interaction.Interact())

