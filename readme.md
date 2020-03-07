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
Try any one of the below: 

A) Grab a release from [github](https://github.com/ManApart/QuestCommand/releases) and run `java -jar ./quest-command-dev.jar`

B) Grab the image from [docker hub]() and run `docker run -it manapart/questcommand:latest`

c) Clone the project and build
From the main folder run `java -jar ./build/libs/quest-command-dev.jar`

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


### Magic

#### Elements
Whether the cause is magic, or just that you lit a tree on fire with your tinderbox, elements will interact with each other. Often, a more powerful element will clear or weaken the duration of effects of a less powerful element of the opposite type.

Name | Clears | Notes
--- | --- | ---
Fire | Water, Ice
Water  | Fire
Earth | Lightning, Fire
Stone |
Air | Water
Air | Earth | Must be twice the strength
Air | Fire |
Air | Fire | If same or lower strength, will buff fire
Lightning | Water | Duration is doubled and water is cleared
Ice | Fire 
Ice | Water | Duration is doubled and water is cleared

#### Spell Types
Different spell types are associated with an element. Some upgraded spells have a unique element that is a combination of multiple base elements (Rain = Air + Water). Spells require a minimum level in their base element to cast. This cost scales with the type of spell and how powerful it is. (Jet may cost 5 water magic to cast. Rain may cast 50 water magic and 30 air magic to cast)

Name | Type | Description  
--- | --- | ---
Water | Base | Health, aoe attacks. Flexible
Air | Base | Quick Attacks and Evasion
Earth | Base | Defense and Slow, Strong Attacks
Fire | Base | Aggressive attacks, self sacrificing attacks
Lightning | Fire Upgrade |
Ice | Water Upgrade |
Stone | Earth Upgrade
Smoke | Air + Fire |
Rain | Air + Water 
Steam | Water + Fire
Mud | Water + Earth
Blizzard | Ice + Air
Storm | Lightning + Air + Water



## Commands

Below is the generated manual for all commands in the game. 
- Names in brackets are params. EX: 'Travel \<location>' should be typed as 'Travel Kanbara'" 
- Words that start with a * are optional
- Commands that end with X are not yet implemented

Category | Command | Aliases | Description | Usages
 --- | --- | --- | --- | --- 
  |  |  | Called when no other command is present | This is the manual for an Unknown Command
 Character | Quest | Quest, Quests, Journal, Q | View your active and completed Quests. | <br/>`Quest active` - View active Quests.<br/>`Quest all` - View all quests.<br/>`Quest <quest>` - View entries for a specific quest.
 Character | Rest | Rest, Sleep, Camp, rs | Rest and regain your stamina. | <br/>`Rest` - Rest for an hour.<br/>`Rest <duration>` - Rest for a set amount of time.
 Character | Status | Status, Info, stats, stat | Get information about your or something else's status | <br/>`Status` - Get your current status<br/>`Status <target>` - Get the status of a target.
 Combat | Approach | Approach, Forward, Advance | Move closer to the enemy. | <br/>`Approach` - Move closer to the enemy (only works in battle)
 Combat | Block | Block | Attempt to block a blow. | <br/>`Block <direction> with <hand>` - Attempt to block a direction with the item in your left/right hand. (Only works in battle).<br/>	You stop blocking next time you choose an action.
 Combat | Cast | Cast, word, c | Speak a word of power. | <br/>`word list` - list known words.<br/>`word <word>` - view the manual for that word.<br/>`Cast <word> <word args> on *<target>` - cast a spell with specific arguments on a target<br/>	Simple Example:<br/>		'Cast shard 5 on bandit'. This would cast an ice shard with 5 points of damage at a random body part of the bandit.<br/>	Complicated Example:<br/>		'Cast shard 5 on left arm chest of bandit and head of rat'. This would cast an ice shard with five damage at the left arm of the bandit, another at the bandit's chest, and a third at the head of the rat.
 Combat | Chop, Slash, Stab | Attack, chop, ch, crush, cr, slash, sl, stab, sb | Chop/Stab/Slash/Crush the target | <br/>`<attack> <target>` - Chop, crush, slash, or stab the target with the item in your right hand<br/>`<attack> <target> with <hand>` - Attack the target with the item in your left/right hand<br/>`<attack> <part> of <target>` - Attack the target, aiming for a specific body part<br/>`<attack> <target> with <item>` - Attack the target with the item in your left/right hand<br/>	Attacking a target damages it based on the chop/stab/slash damage of the item you're holding in that hand, or the damage you do if empty handed
 Combat | Dodge | Dodge | Attempt to dodge a blow. | <br/>`Dodge <direction> *<distance>` - Attempt to dodge a blow (only works in battle).
 Combat | Retreat | Retreat, Backward, Back | Move further from the enemy. | <br/>`Retreat` - Move further from the enemy (only works in battle).
 Crafting | Cook | Cook, Bake | Cook food | <br/>`Cook <ingredient>, <ingredient2> on <range>` - Cook one or more ingredients on a range.
 Crafting | Craft | Craft, Make, Build | Craft a recipe you know | <br/>`Craft <Recipe>` - Craft a recipe.
 Crafting | Recipe | Recipe, Recipes | View your recipes | <br/>`Recipe all` - View the Recipes that you know.<br/>`Recipe <Recipe>` - View the details of a recipe.
 Debug | History | History | History: <br/>View the chat history. | <br/>`History` - View the recent chat history.<br/>`History <number>` - View \<number> lines of chat history X<br/>`History responses` - View both commands and responses X
 Debug | debug | debug, db | Debug: <br/>Change various settings for testing/cheating. | <br/>`Debug` - Toggle various debug settings all on or off at once.<br/>`Debug list` - View the gamestate's properties.<br/>`Debug lvlreq <on/off>` - Toggle the requirement for skills/spells to have a specific level.<br/>`Debug statchanges <on/off>` - Toggle whether stats (stamina, focus, health, etc) can be depleted.<br/>`Debug random <on/off>` - Toggle random chances always succeeding.<br/>`Debug displayupdates <on/off>` - Toggle inline updating display messages (for things like progress bars).<br/>`Debug stat <stat name> <desired level> on *<target>` - Set a stat to the desired level.<br/>`Debug prop <prop name> <desired level> on *<target>` - Set a property to the desired level.<br/>`Debug tag *<remove> <tag name> on *<target>` - Add (or remove) a tag.
 Explore | Look | Look, ls, Examine, Exa | Examine your surroundings | <br/>`Look all` - View the objects you can interact with.<br/>`Look <target>` - Look at a specific target.
 Explore | Map | Map, m | Get information on your current and other locations | <br/>`Map *<location>` - List your current location (or given location) and the surrounding areas.<br/>`Map *depth` - List neighbors to \<depth> levels away from the location.
 Interact | Eat | Eat | Eat an item | <br/>`Eat <item>` - Eat an item
 Interact | Nothing | Nothing, Wait, nn | Nothing<br/>Like resting, but less useful.<br/>Nothing \<duration> - Do nothing for a set amount of time. | <br/>`Nothing` - Do Nothing.
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
 Traveling | Climb | Climb, cl, scale, descend | Climb over obstacles | <br/>`Climb <part> of <target>` - Climb (onto) the target<br/>`Climb <direction>` - Continue climbing in \<direction><br/>`Climb to <part>` - Climb to \<part><br/>`Climb s` - The s flag silences travel, meaning a minimum amount of output
 Traveling | Direction | Direction, NORTH, n, SOUTH, s, WEST, w, EAST, e, NORTH_WEST, nw, NORTH_EAST, ne, SOUTH_WEST, sw, SOUTH_EAST, se, ABOVE, a, BELOW, d, NONE, none | Move to the nearest location in the specified direction. | <br/>`<direction>` - Start moving to the nearest location in that direction, if it exists.<br/>`<direction> s` - The s flag silences travel, meaning a minimum amount of output
 Traveling | Dismount | Dismount, dis | Stop climbing (only at top or bottom of obstacle) | <br/>`Dismount` - Stop climbing (only at top or bottom of obstacle)
 Traveling | Jump | Jump, j | Jump over obstacles or down to a lower area. | <br/>`Jump <obstacle>` - Jump over an obstacle. X<br/>`Jump` - Jump down to the location below, possibly taking damage.
 Traveling | Move | Move, mv, walk, run | Move within locations. | <br/>`Move to <vector>` - Move to a specific place within a location.<br/>`Move to <target>` - Move to a target within a location.<br/>`Move <distance> towards <direction>` - Move a set distance in a direction
 Traveling | Route | Route, rr | View your current Route. | <br/>`Route` - View your current route.<br/>`Route *<location>` - Find a route to \<location>.<br/>	Routes are used with the Move command.
 Traveling | Travel | Travel, t, go, cd | Travel to different locations. | <br/>`Travel to <location>` - Start traveling to a location, if a route can be found.<br/>`Travel` - Continue traveling to a goal location.<br/>`Travel s` - The s flag silences travel, meaning a minimum amount of output<br/>	To view a route, see the Route command

Spell Commands:

Category | Command | Description | Usages
 --- | --- | --- | --- 
 Air | Adrenaline | Increase how fast you can attack. | <br/>`Cast Adrenaline <amount> for <duration> on *<targets>` - Increase action point gain. The higher the amount, faster the action point gain. Hindered by encumbrance and enhanced by higher agility.
 Air | Pull | Pull targets closer to you. | <br/>`Cast Pull <power> on *<targets>` - Pull the targets a set distance closer to you. The higher the power, the further the target will be pulled. Lighter targets are pulled further.<br/>`Cast Pull <power> towards <direction> on *<targets>` - Pull the targets a set distance in the given direction.
 Air | Push | Push targets away from you. | <br/>`Cast Push <power> on *<targets>` - Push the targets away from you. The higher the power, the further the target will be pushed. Lighter targets are pushed further.<br/>`Cast Push <power> towards <direction> on *<targets>` - Push the targets a set distance in the given direction.
 Earth | Rock | Hit the target with a rock. | <br/>`Cast Rock <power> size <size> on <target>` - Hit the target with a rock. Size can be 1, 2, or 3 (small, medium or large). Small size can be rapidly fired while larger sizes do more than linearly more damage, can cause stun, and take longer to fire.
 Earth | Rooted | Encase the target in earth to increase their defense. | <br/>`Cast Rooted <amount> for <duration> on <target>` - Encase the target in earth to increase their defense. Increases defense by amount * percent encumbered. Fully encumbers target.
 Fire | Flame | High damage short range attack that leaves the target burning. | <br/>`Cast Flame <power> on *<targets>` - High damage short range attack that leaves the target burning. Burns caster a proportional amount (minus immunity).
 Water | Heal | Heal yourself or others. | <br/>`Cast Heal <amount> for <duration> on *<targets>` - Heals damage taken over time.
 Water | Jet | Burst of water that does one time damage to one or more targets. | <br/>`Cast Jet <damage amount> on *<targets>` - Burst of water that does one time damage to one or more targets.
 Water | Poison | Poison a target, doing damage over time. | <br/>`Cast Poison <amount> for <duration> on *<targets>` - Does damage over time.
