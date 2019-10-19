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

private val commands: List<core.commands.Command> = listOf(system.RedoCommand(), travel.climb.DismountCommand(), inventory.EquippedCommand(), interact.eat.EatCommand(), combat.dodge.DodgeCommand(), status.journal.JournalCommand(), status.StatusCommand(), status.rest.RestCommand(), inventory.pickupItem.PickupItemCommand(), system.help.HelpCommand(), system.help.CommandsCommand(), explore.map.ReadMapCommand(), combat.block.BlockCommand(), travel.RouteCommand(), interact.interaction.nothing.NothingCommand(), combat.approach.ApproachCommand(), combat.approach.RetreatCommand(), crafting.RecipeCommand(), travel.TravelCommand(), inventory.unEquipItem.UnEquipItemCommand(), interact.UseCommand(), system.history.HistoryCommand(), travel.TravelInDirectionCommand(), inventory.dropItem.PlaceItemCommand(), inventory.InventoryCommand(), combat.attack.AttackCommand(), core.commands.UnknownCommand(), interact.magic.CastCommand(), travel.climb.ClimbCommand(), crafting.CookCommand(), system.ExitCommand(), explore.LookCommand(), inventory.equipItem.EquipItemCommand(), crafting.CraftRecipeCommand(), travel.jump.JumpCommand())

private val spellCommands: List<interact.magic.spellCommands.SpellCommand> = listOf(interact.magic.spellCommands.water.Jet())

private val eventListeners: List<core.events.EventListener<*>> = listOf(interact.eat.EatFood(), travel.jump.PlayerJump(), explore.Look(), interact.actions.UseFoodItem(), combat.block.Block(), travel.ViewRoute(), inventory.pickupItem.ItemPickedUp(), combat.block.StartBlock(), inventory.dropItem.ItemDropped(), interact.actions.StartFire(), travel.Arrive(), status.statChanged.PlayerStatMinned(), travel.jump.PlayerFall(), status.effects.AddCondition(), crafting.Craft(), interact.actions.UseIngredientOnActivatorRecipe(), interact.interaction.Interact(), interact.magic.ViewWordHelp(), core.gameState.SetProperties(), interact.actions.NoUseFound(), travel.climb.AttemptClimb(), core.gameState.quests.SetQuestStage(), status.rest.Rest(), combat.takeDamage.TakeDamage(), combat.dodge.Dodge(), combat.battle.BattleTurn(), inventory.unEquipItem.ItemUnEquipped(), combat.battle.BattleTick(), status.journal.ViewQuestJournal(), status.effects.RemoveCondition(), interact.actions.ScratchSurface(), interact.actions.UseOnFire(), travel.climb.ClimbComplete(), combat.attack.Attack(), status.journal.ViewQuestList(), combat.attack.StartAttack(), explore.RestrictLocation(), interact.actions.ChopWood(), system.gameTick.TimeManager(), combat.battle.CombatantDied(), interact.scope.spawn.SpawnItem(), status.statChanged.PlayerStatMaxed(), travel.FindRoute(), status.Status(), status.statChanged.StatBoosted(), system.help.ViewHelp(), crafting.DiscoverRecipe(), system.history.ViewChatHistory(), interact.actions.StatMinned(), interact.scope.ArrivalHandler(), status.statChanged.CreatureDied(), travel.TravelStart(), crafting.CheckRecipes(), interact.magic.CastSpell(), explore.map.ReadMap(), combat.dodge.StartDodge(), inventory.equipItem.ItemEquipped(), status.LevelUp(), interact.scope.spawn.ActivatorSpawner(), core.gameState.quests.QuestListener(), combat.approach.Move(), interact.interaction.nothing.DoNothing(), status.ExpGained(), interact.actions.DamageCreature(), interact.interaction.nothing.StartNothing(), inventory.unEquipItem.UnEquipItem(), system.GameManager.MessageHandler(), status.statChanged.StatChanged(), status.effects.ApplyEffects(), interact.scope.remove.RemoveItem(), interact.interaction.NoInteractionFound(), inventory.ListInventory(), combat.approach.StartMove(), inventory.equipItem.EquipItem(), system.item.ItemSpawner(), system.gameTick.GameTick(), interact.actions.UseItemOnIngredientRecipe(), inventory.dropItem.TransferItem(), interact.scope.remove.RemoveScope(), interact.magic.StartCastSpell(), core.gameState.quests.CompleteQuest())

