# Quest Command

[![Build Status](https://travis-ci.org/ManApart/QuestCommand.svg?branch=master)](https://travis-ci.org/ManApart/QuestCommand)

An open world rpg with intense levels of interaction, experienced through the command prompt.

This doc covers the overview of Quest Command and is focused on the user/player.
For in-development feature notes see [the notes](./notes.md). For system and design explanations, see the [creator's readme](./design_doc.md)

## Building / Running

### Building

Run `gradlew buildData`. This generates json files etc so that they don't need to be done at runtime. This only needs to be re-run when you change certain files (like adding new commands, events, event listeners, changing json, etc)

Run `gradlew build jar` to build a jar. This should be found in `QuestCommand/build/libs` (`quest-command-1.0-SNAPSHOT`)


### Running
From the main folder run `java -jar ./build/libs/quest-command-1.0-SNAPSHOT.jar`

## What can I do?

At this point there is very little solid functionality, but there are a few basic things you can do.

Start by typing `help`, which should get you started and give you a list of commands.

Try using commands like `look` and `map` to explore the world.

You can travel by traveling to a location `travel tree`, or you can just say a direction (`west` or `w`).

Try finding the apple tree and picking up some apples, climbing it, or lighting it on fire with your tinder box. You could also chop it down if you wanted to.

There is very basic inventory functionality and you can equip weapons to different hands.

You can find and kill a rat, and then use its meat on the range in the nearby farm house in order to cook it.

You can also pipe commands together using `&&`.

Here are some example commands you can run:
- `w && use tinder on tree && use apple on tree`
- `w && use tinder on tree && bag && pickup apple && bag && use apple on tree`
- `travel tree && climb tree`
- `travel an open field barren patch && equip hatchet && chop rat && slash rat`
- `travel farmer's hut interior && cook Raw Poor Quality Meat on range`


## Implemented Systems

These systems may have limited content and are probably really buggy, but __should__ work today.

### Inventory

Players can see their own inventory and the inventory of any target that has the `Container` tag.

They can take objects from any container that has the `Container` and `Open` keywords.

They can place items in a container if it has the `Container` and `Open` tags and if the container has remaining capacity. Remaining capacity is determined by a creature's current strength level, or the container's `Capacity` value.

Containers may have a `CanHold` value: `"CanHold": "Dagger,Bow"`. If a container does not have a `CanHold` property then any type of item can be placed in it. If it does have the property, then only items that have at least one matching tag can be placed in the container. For example, pouches and sacks can hold any type of item, while holsters may only hold swords or maces.


## Commands

Below is the generated manual for all commands in the game. 
- Names in brackets are params. EX: 'Travel \<location>' should be typed as 'Travel Kanbara'" 
- Words that start with a * are optional
- Commands that end with X are not yet implemented

Category | Command | Aliases | Description | Usages
 --- | --- | --- | --- | --- 
 Character | Quest | Quest, Quests, Journal, Q | View your active and completed Quests. | <br/>`Quest active` - View active Quests.<br/>`Quest all` - View all quests.<br/>`Quest <quest>` - View entries for a specific quest.
 Character | Rest | Rest, Sleep, Camp, Wait, rs | Rest and regain your stamina. | <br/>`Rest` - Rest for an hour.<br/>`Rest <amount>` - Rest for a set amount of time.
 Character | Status | Status, Info, stats, stat | Get information about your or something else's status | <br/>`Status` - Get your current status<br/>`Status <target>` - Get the status of a target.
 Combat | Approach | Approach, Forward, Advance | Move closer to the enemy. | <br/>`Approach` - Move closer to the enemy (only works in battle)
 Combat | Block | Block | Attempt to block a blow. | <br/>`Block <direction> with <hand>` - Attempt to block a direction with the item in your left/right hand. (Only works in battle).<br/>	You stop blocking next time you choose an action.
 Combat | Cast | Cast, word | Speak a word of power. | <br/>`word list` - list known words.<br/>`word <word>` - view the manual for that word.<br/>`Cast <word> <word args> on *<target>` - cast a spell with specific arguments on a target<br/>	Simple Example:<br/>		'Cast shard 5 on bandit'. This would cast an ice shard with 5 points of damage at a random body part of the bandit.<br/>	Complicated Example:<br/>		'Cast shard 5 on left arm chest of bandit and head of rat'. This would cast an ice shard with five damage at the left arm of the bandit, another at the bandit's chest, and a third at the head of the rat.
 Combat | Chop, Slash, Stab | Attack, chop, ch, crush, cr, slash, sl, stab, sb | Chop/Stab/Slash/Crush the target | <br/>`<attack> <target>` - Chop, crush, slash, or stab the target with the item in your right hand<br/>`<attack> <target> with <hand>` - Attack the target with the item in your left/right hand<br/>`<attack> <part> of <target>` - Attack the target, aiming for a specific body part<br/>`<attack> <target> with <item>` - Attack the target with the item in your left/right hand<br/>	Attacking a target damages it based on the chop/stab/slash damage of the item you're holding in that hand, or the damage you do if empty handed
 Combat | Dodge | Dodge | Attempt to dodge a blow. | <br/>`Dodge <direction> *<distance>` - Attempt to dodge a blow (only works in battle).
 Combat | Retreat | Retreat, Backward, Back | Move further from the enemy. | <br/>`Retreat` - Move further from the enemy (only works in battle).
 Crafting | Cook | Cook, Bake | Craft food | <br/>`Craft <ingredient>, <ingredient2> on <range>` - Craft one or more ingredients on a range.
 Crafting | Craft | Craft, Make, Build | Craft a recipe you know | <br/>`Craft <Recipe>` - Craft a recipe.
 Crafting | Recipe | Recipe, Recipes | View your recipes | <br/>`Recipe all` - View the Recipes that you know.<br/>`Recipe <Recipe>` - View the details of a recipe.
 Debug | History | History | History: <br/>View the chat history. | <br/>`History` - View the recent chat history.<br/>`History <number>` - View \<number> lines of chat history X<br/>`History responses` - View both commands and responses X
 Explore | Look | Look, ls, Examine, Exa | Examine your surroundings | <br/>`Look all` - View the objects you can interact with.<br/>`Look <target>` - Look at a specific target.
 Explore | Map | Map, m | Get information on your current and other locations | <br/>`Map *<location>` - List your current location (or given location) and the surrounding areas.<br/>`Map *depth` - List neighbors to \<depth> levels away from the location.
 Interact | Eat | Eat | Eat an item | <br/>`Eat <item>` - Eat an item
 Interact | Nothing | Nothing | Nothing<br/>Like resting, but less useful | <br/>`Nothing` - Do Nothing
 Interact | Use | Use, u, Read | Use an item or your surroundings | <br/>`Use <item>` - Interact with an item or target<br/>`Use <item> on <target>` - Use an item on a target.
 Inventory | Bag | Bag, b, backpack | View and manage your inventory. | <br/>`Bag` - list items in your inventory.<br/>`Bag <target>` - list items in the target's inventory, if possible.
 Inventory | Equip | Equip | Equip an item from your inventory | <br/>`Equip <item>` - Equip an item<br/>`Equip <item> to <body part>` - Equip an item to a specific body part (ex: left hand). X<br/>`Equip <item> to <body part> f` - Equip an item even if that means unequipping what's already equipped there. X
 Inventory | Equipped | Equipped | View what you currently have equipped | <br/>`Equipped` - View what you currently have equipped
 Inventory | Pickup | Pickup, p, get, add, take, grab | Add an item to your inventory. | <br/>`Pickup <item>` - pickup an item.<br/>`Pickup <item> from <target>` - take item from target's inventory, if possible.
 Inventory | Place | Place, Drop, Give, Put | Place an item from your inventory in another inventory or on the ground. | <br/>`Drop <item>` - Drop an item an item from your inventory.<br/>`Place <item> in <target>` - Drop an item an item from your inventory.
 Inventory | UnEquip | UnEquip | UnEquip an item you're wearing | <br/>`UnEquip <item>` - UnEquip an item<br/>`UnEquip <body part>` - UnEquip any items worn on a specific body part (ex: left hand) X
 System | Commands | Commands | Commands: <br/>Commands \<Group> \<Command> - Execute a command. | Commands: <br/>`Commands <Group> <Command>` - Execute a command.
 System | Exit | Exit, Quit, qqq | Exit the program. | <br/>`Exit` - Exit the program.
 System | Help | Help, h | Names in brackets are params. EX: 'Travel \<location>' should be typed as 'Travel Kanbara'<br/>Words that start with a * are optional<br/>Commands that end with X are not yet implemented | Help: <br/>`Help All` - Return general help<br/>`Help Commands` - Return a list of other types of commands that can be called.<br/>`Help Commands extended` - Return a list of commands and all their aliases.<br/>`Help <Command Group>` - Return the list of commands within a group of commands<br/>`Help <Command>` - Return the manual for that command<br/>Notes:<br/>	Names in brackets are params. EX: 'Travel <location>' should be typed as 'Travel Kanbara'<br/>	Words that start with a * are optional<br/>	Commands that end with X are not yet implemented
 System | Redo | Redo, Repeat, r | Redo your last command. | Redo:<br/>	Redo your last command.
 System | Save | Save, sa | Save your game. | <br/>`Save` - Save your game X<br/>`Save <name>` - Save with a specific save name. X
 Traveling | Climb | Climb, c, scale, descend | Climb over obstacles | <br/>`Climb <part> of <target>` - Climb (onto) the target<br/>`Climb <direction>` - Continue climbing in \<direction><br/>`Climb to <part>` - Climb to \<part><br/>`Climb s` - The s flag silences travel, meaning a minimum amount of output
 Traveling | Direction | Direction, NORTH, n, SOUTH, s, WEST, w, EAST, e, NORTH_WEST, nw, NORTH_EAST, ne, SOUTH_WEST, sw, SOUTH_EAST, se, ABOVE, a, BELOW, d, NONE, none | Move to the nearest location in the specified direction. | <br/>`<direction>` - Start moving to the nearest location in that direction, if it exists.<br/>`<direction> s` - The s flag silences travel, meaning a minimum amount of output
 Traveling | Dismount | Dismount, dis | Stop climbing (only at top or bottom of obstacle) | <br/>`Dismount` - Stop climbing (only at top or bottom of obstacle)
 Traveling | Jump | Jump, j | Jump over obstacles or down to a lower area. | <br/>`Jump <obstacle>` - Jump over an obstacle. X<br/>`Jump` - Jump down to the location below, possibly taking damage.
 Traveling | Route | Route, rr | View your current Route. | <br/>`Route` - View your current route.<br/>`Route *<location>` - Find a route to \<location>.<br/>	Routes are used with the Move command.
 Traveling | Travel | Travel, move, t, go, mv, cd | Travel to different locations. | <br/>`Travel to <location>` - Start traveling to a location, if a route can be found.<br/>`Travel` - Continue traveling to a goal location.<br/>`Travel s` - The s flag silences travel, meaning a minimum amount of output<br/>	To view a route, see the Route command

Spell Commands:

Category | Command | Description | Usages
 --- | --- | --- | --- 
 Water | Jet | Burst of water that does one time damage to one or more targets. | <br/>`Cast Jet <damage amount> on *<targets>` - Burst of water that does one time damage to one or more targets.
