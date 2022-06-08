package core.events

class EventListenerMapGenerated : EventListenerMapCollection {
    private val listenerMap: Map<String, EventListener<*>> = mapOf("combat.attack.Attack" to combat.attack.Attack(), "combat.block.Block" to combat.block.Block(), "combat.takeDamage.TakeDamage" to combat.takeDamage.TakeDamage(), "conversation.dialogue.DialogueListener" to conversation.dialogue.DialogueListener(), "conversation.end.EndConversation" to conversation.end.EndConversation(), "conversation.start.StartConversation" to conversation.start.StartConversation(), "core.MessageHandler" to core.MessageHandler(), "core.ai.AIGameTickListener" to core.ai.AIGameTickListener(), "core.ai.AITurnDirector" to core.ai.AITurnDirector(), "core.ai.DelayedEventListener" to core.ai.DelayedEventListener(), "core.commands.commandEvent.CommandEventListener" to core.commands.commandEvent.CommandEventListener(), "core.events.multiEvent.MultiEventListener" to core.events.multiEvent.MultiEventListener(), "core.history.SessionListener" to core.history.SessionListener(), "core.properties.SetProperties" to core.properties.SetProperties(), "core.properties.propValChanged.PropertyStatChanged" to core.properties.propValChanged.PropertyStatChanged(), "core.properties.propValChanged.PropertyStatMinned" to core.properties.propValChanged.PropertyStatMinned(), "core.thing.item.ItemSpawner" to core.thing.item.ItemSpawner(), "crafting.DiscoverRecipe" to crafting.DiscoverRecipe(), "crafting.checkRecipe.CheckRecipes" to crafting.checkRecipe.CheckRecipes(), "crafting.craft.Craft" to crafting.craft.Craft(), "explore.examine.Examine" to explore.examine.Examine(), "explore.listen.Listen" to explore.listen.Listen(), "explore.look.Look" to explore.look.Look(), "explore.map.ReadMap" to explore.map.ReadMap(), "explore.map.compass.SetCompassGoal" to explore.map.compass.SetCompassGoal(), "explore.map.compass.ViewCompass" to explore.map.compass.ViewCompass(), "inventory.ViewEquipped" to inventory.ViewEquipped(), "inventory.ViewInventory" to inventory.ViewInventory(), "inventory.dropItem.ItemDropped" to inventory.dropItem.ItemDropped(), "inventory.dropItem.PlaceItem" to inventory.dropItem.PlaceItem(), "inventory.equipItem.EquipItem" to inventory.equipItem.EquipItem(), "inventory.equipItem.ItemEquipped" to inventory.equipItem.ItemEquipped(), "inventory.pickupItem.ItemPickedUp" to inventory.pickupItem.ItemPickedUp(), "inventory.pickupItem.TakeItem" to inventory.pickupItem.TakeItem(), "inventory.putItem.TransferItem" to inventory.putItem.TransferItem(), "inventory.unEquipItem.ItemUnEquipped" to inventory.unEquipItem.ItemUnEquipped(), "inventory.unEquipItem.UnEquipItem" to inventory.unEquipItem.UnEquipItem(), "magic.ViewWordHelp" to magic.ViewWordHelp(), "magic.castSpell.CastSpell" to magic.castSpell.CastSpell(), "quests.CompleteQuest" to quests.CompleteQuest(), "quests.QuestListener" to quests.QuestListener(), "quests.journal.ViewQuestJournal" to quests.journal.ViewQuestJournal(), "quests.journal.ViewQuestList" to quests.journal.ViewQuestList(), "status.ExpGained" to status.ExpGained(), "status.LevelUp" to status.LevelUp(), "status.conditions.AddCondition" to status.conditions.AddCondition(), "status.conditions.ConditionRemover" to status.conditions.ConditionRemover(), "status.conditions.RemoveCondition" to status.conditions.RemoveCondition(), "status.effects.ApplyEffects" to status.effects.ApplyEffects(), "status.rest.Rest" to status.rest.Rest(), "status.statChanged.CreatureDied" to status.statChanged.CreatureDied(), "status.statChanged.PlayerStatMaxed" to status.statChanged.PlayerStatMaxed(), "status.statChanged.PlayerStatMinned" to status.statChanged.PlayerStatMinned(), "status.statChanged.StatBoosted" to status.statChanged.StatBoosted(), "status.statChanged.StatChanged" to status.statChanged.StatChanged(), "status.statChanged.StatMinned" to status.statChanged.StatMinned(), "status.status.Status" to status.status.Status(), "system.alias.CreateAlias" to system.alias.CreateAlias(), "system.alias.DeleteAlias" to system.alias.DeleteAlias(), "system.alias.ListAlias" to system.alias.ListAlias(), "system.connection.Connect" to system.connection.Connect(), "system.connection.ConnectInfo" to system.connection.ConnectInfo(), "system.connection.Disconnect" to system.connection.Disconnect(), "system.debug.DebugListListener" to system.debug.DebugListListener(), "system.debug.DebugStatListener" to system.debug.DebugStatListener(), "system.debug.DebugTagListener" to system.debug.DebugTagListener(), "system.debug.DebugToggleListener" to system.debug.DebugToggleListener(), "system.debug.DebugWeatherListener" to system.debug.DebugWeatherListener(), "system.help.ViewHelp" to system.help.ViewHelp(), "system.history.ViewGameLog" to system.history.ViewGameLog(), "system.message.DisplayMessage" to system.message.DisplayMessage(), "system.persistance.changePlayer.ListCharacters" to system.persistance.changePlayer.ListCharacters(), "system.persistance.changePlayer.PlayAs" to system.persistance.changePlayer.PlayAs(), "system.persistance.createPlayer.CreateCharacter" to system.persistance.createPlayer.CreateCharacter(), "system.persistance.loading.ListSaves" to system.persistance.loading.ListSaves(), "system.persistance.loading.Load" to system.persistance.loading.Load(), "system.persistance.newGame.CreateNewGame" to system.persistance.newGame.CreateNewGame(), "system.persistance.saving.Save" to system.persistance.saving.Save(), "time.ViewTime" to time.ViewTime(), "time.gameTick.GameTick" to time.gameTick.GameTick(), "time.gameTick.TimeListener" to time.gameTick.TimeListener(), "traveling.RestrictLocation" to traveling.RestrictLocation(), "traveling.arrive.ArrivalHandler" to traveling.arrive.ArrivalHandler(), "traveling.arrive.Arrive" to traveling.arrive.Arrive(), "traveling.climb.AttemptClimb" to traveling.climb.AttemptClimb(), "traveling.climb.ClimbComplete" to traveling.climb.ClimbComplete(), "traveling.jump.PlayerFall" to traveling.jump.PlayerFall(), "traveling.jump.PlayerJump" to traveling.jump.PlayerJump(), "traveling.location.weather.WeatherListener" to traveling.location.weather.WeatherListener(), "traveling.move.Move" to traveling.move.Move(), "traveling.routes.FindRoute" to traveling.routes.FindRoute(), "traveling.routes.ViewRoute" to traveling.routes.ViewRoute(), "traveling.scope.remove.RemoveItem" to traveling.scope.remove.RemoveItem(), "traveling.scope.remove.RemoveScope" to traveling.scope.remove.RemoveScope(), "traveling.scope.spawn.ActivatorSpawner" to traveling.scope.spawn.ActivatorSpawner(), "traveling.scope.spawn.SpawnItem" to traveling.scope.spawn.SpawnItem(), "traveling.travel.TravelStart" to traveling.travel.TravelStart(), "use.actions.ChopWood" to use.actions.ChopWood(), "use.actions.DamageCreature" to use.actions.DamageCreature(), "use.actions.NoUseFound" to use.actions.NoUseFound(), "use.actions.ScratchSurface" to use.actions.ScratchSurface(), "use.actions.StartFire" to use.actions.StartFire(), "use.actions.UseFoodItem" to use.actions.UseFoodItem(), "use.actions.UseIngredientOnActivatorRecipe" to use.actions.UseIngredientOnActivatorRecipe(), "use.actions.UseItemOnIngredientRecipe" to use.actions.UseItemOnIngredientRecipe(), "use.actions.UseOnFire" to use.actions.UseOnFire(), "use.eat.EatFood" to use.eat.EatFood(), "use.interaction.Interact" to use.interaction.Interact(), "use.interaction.NoInteractionFound" to use.interaction.NoInteractionFound(), "use.interaction.nothing.DoNothing" to use.interaction.nothing.DoNothing())
    
    override val values: Map<String, List<EventListener<*>>> = mapOf("AttackEvent" to listOf(listenerMap["combat.attack.Attack"]!!), "BlockEvent" to listOf(listenerMap["combat.block.Block"]!!), "TakeDamageEvent" to listOf(listenerMap["combat.takeDamage.TakeDamage"]!!), "DialogueEvent" to listOf(listenerMap["conversation.dialogue.DialogueListener"]!!), "EndConversationEvent" to listOf(listenerMap["conversation.end.EndConversation"]!!), "StartConversationEvent" to listOf(listenerMap["conversation.start.StartConversation"]!!), "MessageEvent" to listOf(listenerMap["core.MessageHandler"]!!), "GameTickEvent" to listOf(listenerMap["core.ai.AIGameTickListener"]!!,listenerMap["status.conditions.ConditionRemover"]!!,listenerMap["status.effects.ApplyEffects"]!!,listenerMap["time.gameTick.TimeListener"]!!,listenerMap["traveling.location.weather.WeatherListener"]!!), "AIUpdateTick" to listOf(listenerMap["core.ai.AITurnDirector"]!!), "Event" to listOf(listenerMap["core.ai.DelayedEventListener"]!!,listenerMap["core.history.SessionListener"]!!,listenerMap["quests.QuestListener"]!!,listenerMap["time.gameTick.GameTick"]!!), "CommandEvent" to listOf(listenerMap["core.commands.commandEvent.CommandEventListener"]!!), "MultiEvent" to listOf(listenerMap["core.events.multiEvent.MultiEventListener"]!!), "SetPropertiesEvent" to listOf(listenerMap["core.properties.SetProperties"]!!), "PropertyStatChangeEvent" to listOf(listenerMap["core.properties.propValChanged.PropertyStatChanged"]!!), "PropertyStatMinnedEvent" to listOf(listenerMap["core.properties.propValChanged.PropertyStatMinned"]!!), "SpawnItemEvent" to listOf(listenerMap["core.thing.item.ItemSpawner"]!!), "DiscoverRecipeEvent" to listOf(listenerMap["crafting.DiscoverRecipe"]!!), "CheckRecipeEvent" to listOf(listenerMap["crafting.checkRecipe.CheckRecipes"]!!), "CraftRecipeEvent" to listOf(listenerMap["crafting.craft.Craft"]!!), "ExamineEvent" to listOf(listenerMap["explore.examine.Examine"]!!), "ListenEvent" to listOf(listenerMap["explore.listen.Listen"]!!), "LookEvent" to listOf(listenerMap["explore.look.Look"]!!), "ReadMapEvent" to listOf(listenerMap["explore.map.ReadMap"]!!), "SetCompassEvent" to listOf(listenerMap["explore.map.compass.SetCompassGoal"]!!), "ViewCompassEvent" to listOf(listenerMap["explore.map.compass.ViewCompass"]!!), "ViewEquippedEvent" to listOf(listenerMap["inventory.ViewEquipped"]!!), "ViewInventoryEvent" to listOf(listenerMap["inventory.ViewInventory"]!!), "ItemDroppedEvent" to listOf(listenerMap["inventory.dropItem.ItemDropped"]!!), "PlaceItemEvent" to listOf(listenerMap["inventory.dropItem.PlaceItem"]!!), "EquipItemEvent" to listOf(listenerMap["inventory.equipItem.EquipItem"]!!), "ItemEquippedEvent" to listOf(listenerMap["inventory.equipItem.ItemEquipped"]!!), "ItemPickedUpEvent" to listOf(listenerMap["inventory.pickupItem.ItemPickedUp"]!!), "TakeItemEvent" to listOf(listenerMap["inventory.pickupItem.TakeItem"]!!), "TransferItemEvent" to listOf(listenerMap["inventory.putItem.TransferItem"]!!), "ItemUnEquippedEvent" to listOf(listenerMap["inventory.unEquipItem.ItemUnEquipped"]!!), "UnEquipItemEvent" to listOf(listenerMap["inventory.unEquipItem.UnEquipItem"]!!), "ViewWordHelpEvent" to listOf(listenerMap["magic.ViewWordHelp"]!!), "CastSpellEvent" to listOf(listenerMap["magic.castSpell.CastSpell"]!!), "CompleteQuestEvent" to listOf(listenerMap["quests.CompleteQuest"]!!), "ViewQuestJournalEvent" to listOf(listenerMap["quests.journal.ViewQuestJournal"]!!), "ViewQuestListEvent" to listOf(listenerMap["quests.journal.ViewQuestList"]!!), "ExpGainedEvent" to listOf(listenerMap["status.ExpGained"]!!), "LevelUpEvent" to listOf(listenerMap["status.LevelUp"]!!), "AddConditionEvent" to listOf(listenerMap["status.conditions.AddCondition"]!!), "RemoveConditionEvent" to listOf(listenerMap["status.conditions.RemoveCondition"]!!), "RestEvent" to listOf(listenerMap["status.rest.Rest"]!!), "StatMinnedEvent" to listOf(listenerMap["status.statChanged.CreatureDied"]!!,listenerMap["status.statChanged.PlayerStatMinned"]!!,listenerMap["status.statChanged.StatMinned"]!!), "StatMaxedEvent" to listOf(listenerMap["status.statChanged.PlayerStatMaxed"]!!), "StatBoostEvent" to listOf(listenerMap["status.statChanged.StatBoosted"]!!), "StatChangeEvent" to listOf(listenerMap["status.statChanged.StatChanged"]!!), "StatusEvent" to listOf(listenerMap["status.status.Status"]!!), "CreateAliasEvent" to listOf(listenerMap["system.alias.CreateAlias"]!!), "DeleteAliasEvent" to listOf(listenerMap["system.alias.DeleteAlias"]!!), "ListAliasesEvent" to listOf(listenerMap["system.alias.ListAlias"]!!), "ConnectEvent" to listOf(listenerMap["system.connection.Connect"]!!), "ConnectInfoEvent" to listOf(listenerMap["system.connection.ConnectInfo"]!!), "DisconnectEvent" to listOf(listenerMap["system.connection.Disconnect"]!!), "DebugListEvent" to listOf(listenerMap["system.debug.DebugListListener"]!!), "DebugStatEvent" to listOf(listenerMap["system.debug.DebugStatListener"]!!), "DebugTagEvent" to listOf(listenerMap["system.debug.DebugTagListener"]!!), "DebugToggleEvent" to listOf(listenerMap["system.debug.DebugToggleListener"]!!), "DebugWeatherEvent" to listOf(listenerMap["system.debug.DebugWeatherListener"]!!), "ViewHelpEvent" to listOf(listenerMap["system.help.ViewHelp"]!!), "ViewGameLogEvent" to listOf(listenerMap["system.history.ViewGameLog"]!!), "DisplayMessageEvent" to listOf(listenerMap["system.message.DisplayMessage"]!!), "ListCharactersEvent" to listOf(listenerMap["system.persistance.changePlayer.ListCharacters"]!!), "PlayAsEvent" to listOf(listenerMap["system.persistance.changePlayer.PlayAs"]!!), "CreateCharacterEvent" to listOf(listenerMap["system.persistance.createPlayer.CreateCharacter"]!!), "ListSavesEvent" to listOf(listenerMap["system.persistance.loading.ListSaves"]!!), "LoadEvent" to listOf(listenerMap["system.persistance.loading.Load"]!!), "CreateNewGameEvent" to listOf(listenerMap["system.persistance.newGame.CreateNewGame"]!!), "SaveEvent" to listOf(listenerMap["system.persistance.saving.Save"]!!), "ViewTimeEvent" to listOf(listenerMap["time.ViewTime"]!!), "RestrictLocationEvent" to listOf(listenerMap["traveling.RestrictLocation"]!!), "ArriveEvent" to listOf(listenerMap["traveling.arrive.ArrivalHandler"]!!,listenerMap["traveling.arrive.Arrive"]!!), "AttemptClimbEvent" to listOf(listenerMap["traveling.climb.AttemptClimb"]!!), "ClimbCompleteEvent" to listOf(listenerMap["traveling.climb.ClimbComplete"]!!), "FallEvent" to listOf(listenerMap["traveling.jump.PlayerFall"]!!), "JumpEvent" to listOf(listenerMap["traveling.jump.PlayerJump"]!!), "MoveEvent" to listOf(listenerMap["traveling.move.Move"]!!), "FindRouteEvent" to listOf(listenerMap["traveling.routes.FindRoute"]!!), "ViewRouteEvent" to listOf(listenerMap["traveling.routes.ViewRoute"]!!), "RemoveItemEvent" to listOf(listenerMap["traveling.scope.remove.RemoveItem"]!!), "RemoveScopeEvent" to listOf(listenerMap["traveling.scope.remove.RemoveScope"]!!), "SpawnActivatorEvent" to listOf(listenerMap["traveling.scope.spawn.ActivatorSpawner"]!!), "ItemSpawnedEvent" to listOf(listenerMap["traveling.scope.spawn.SpawnItem"]!!), "TravelStartEvent" to listOf(listenerMap["traveling.travel.TravelStart"]!!), "UseEvent" to listOf(listenerMap["use.actions.ChopWood"]!!,listenerMap["use.actions.DamageCreature"]!!,listenerMap["use.actions.NoUseFound"]!!,listenerMap["use.actions.ScratchSurface"]!!,listenerMap["use.actions.StartFire"]!!,listenerMap["use.actions.UseFoodItem"]!!,listenerMap["use.actions.UseIngredientOnActivatorRecipe"]!!,listenerMap["use.actions.UseItemOnIngredientRecipe"]!!,listenerMap["use.actions.UseOnFire"]!!), "EatFoodEvent" to listOf(listenerMap["use.eat.EatFood"]!!), "InteractEvent" to listOf(listenerMap["use.interaction.Interact"]!!,listenerMap["use.interaction.NoInteractionFound"]!!), "NothingEvent" to listOf(listenerMap["use.interaction.nothing.DoNothing"]!!))
}