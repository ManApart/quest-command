# Quest Command

An open world rpg with intense levels of interaction, experienced through the command prompt

This doc should be in constant flux; it's a collection of ideas and notes for dreamed of or in progress features. As features are implemented, any necessary feature explanation should move to [the design doc](./design_doc.md) and any user facing explanation should move to the [main readme](readme.md).

## Planning and Ideas

While the readme should cover how to build and run the game, as well with information on how the game works, these notes are the design docs of the game and should frequently change and be deleted (and changed to readme documentation) once the feature is implemented.

### AI

Rat AI
  - only attack with low aim
  - Approach dagger range if not already in dagger range
  
Combat training dummy
- Player controlled ai
- Used to test fights

Player Controlled AI
- All commands have an execute(source, keyword, args). Default delegates to execute(keyword, args). Supported commands create their event using source instead of player.
- Command parser is passed the Target that is issuing the command. 99% of the time it’s the player, but could be a player controlled AI.



### Architecture

- Set target copy's location to spawned location
- Reset vs Reload. Reset resets game state, reload reloads json?
- Flow of param overrides (left overrides right)
  - Item Params > inheritable params > Behavior Params/Properties > Conditions/events
- make evaluate and execute return true/ a number so caller can be aware if behavior etc found
- Managers etc should return copies of items so that the parsed ones are immutable
- Create a git wiki and move readme information to more sorted format?
- Validate valid recipe ingredients and results
- Break triggered event class out into multiple classes
- delete existing generated files on buildData so renamed files don't leave artifacts
- double check body parts are properly instanced through scope manager
- For now hardcoding commands and triggers to use the player's location parent for network. Eventually that should only be a default

Json
- Inherit object that's common to item and activator (burnable)?

Paramitization
- paramatize targets on spawn events
- delete behavior recipe / rework for global params
- replace non-parameratized $keys?
- paramatize numbers/boolean like weight?
- follow param pattern with creatures
- Eventually param locations and location nodes. Ex: Location Node windmill can pass grain bin node location down to chute.

Command parsing
- Sub command parser / command parser can operate on sub-commands (like spell commands)? Probably not worth it unless we have more sub-commands than casting spells.

Player.isKnown(command)
- Checks against list of known commands
- First populated by reading through all commands and finding ones that have initiallyKnown set to true (default is false)

Codebase stats
- Lines of code
- Number of commands, events, listeners

Rename dynamicDialogue to dynamic string

Turn bodypart into just location
- Equipped items are just the items in that location
- Slots could be location poperties

#### Debug Mode
When on
- All locations revealed on map
- All commands unlocked

Debug command is hidden by default, but can be called to be turned on
Command line arg can also turn debug on at start

Commands can be hidden, locked, or unlocked
State is stored per command or in game properties?
Hidden commands can still be called, but don’t show up in help
Locked commands give error if called (you don’t know how to do that)

GameState has Values file for game level properties, like if debug is on or off

### Combat

Should be reworked to use new body system

#### Thoughts and Description

Battle commands (attack, defend etc) allow a hand to be specified or default to the hand that has the weapon with the most damage etc for that skill

Battles take place at different distances (knife, sword, lance, bow). Combatants can step closer or further back during their turn.

Each attack is given a target position: (high-left, mid-right etc). Combatants can use dodge (jump, duck, left, right) to temporarily adjust their position. They can also use block to block a specific position (the shield passively blocks the slot it's equipped to).

When an attack lands it picks a body part from the target position. If there are no body parts in the target position, it looks for one from any of the adjacent slots. Full damage is done to a slot at the attack target position, and half damage is done to a slot in adjacent positions.

Damage is assessed by getting the slots at a specific position and then working through each layer of equipped armor on that slot. Each layer subtracts its defense from total damage done. The leftover damage detracts the combatant's health.

Armour has critical percents. A critical is a hole in the armour where armor damage is ignored.

Balanced weapons are things like swords and daggers. Weighted weapons are things like maces, axes, and hammers.

What if every action takes action points and you can take a turn every time you have 0 or more action points. Wisdom effects how many action points you get back every battle tick. So slashing with a dagger takes 2 action points, while swinging a war-hammer takes 50.

**Battle Skills**
- Archery
- Block
- Dodge
  - Crouch left
  - Jump right
- Large Balanced Weapons
- Large Weighted Weapons
- Small Balanced Weapons Left Handed
- Small Balanced Weapons Right Handed
- Small Weighted Weapons Left Handed
- Small Weighted Weapons Right Handed

#### Charts

**Stat Effects**

Stat | Effect
--- | ---
Agility | how long an action takes
Strength | how much the attack does
Encumbrance | how long an action takes
Perception | chance of critical
Wisdom | how often someone can choose an action

**Attack Types**

Command | Speed | Damage | Focus
--- | --- | --- | ---
Slash | Fastest | Lowest | Quick Strikes
Stab | Fast | Low | High Critical
Chop | Medium | High | Consistent Damage
Crush | Slow | Highest | High Damage


#### Turn Walk Through
Each Turn
- Increase combatants AP by their wisdom level until one person hits 100
- Combatant choose an attack or any other command (AP reset to 0)
  - Equip item - instant
  - Use Item - instant
  - Dodge - takes time
  - Approach/Retreat - takes time
  - Block - takes time
  - Attack - weapon size + weapon weight + encumberance - agility
  - Wait - instant and keeps AP point at 100
- Weapon Size + weight + encumbrance percent determines number of turns taken to deliver the attack
- Each action drains stamina. The heavier the weapon the more stamina drained for an attack, the more encumbered the more stamina drained per movement
- Without enough stamina the combatant can’t do anything
- A Combatant can rest to recover stamina points

#### Other
- Boss that you fight by climbing, hitting, getting thrown off, taking fall damage, repeating
- Attack direction should use previous target
- Individual health for body parts, can't use parts that don't have health?
- slash should attack all objects within weapon range of root object

Maybe attack listeners delegate to battle to be told if to execute or not. Battle either stores them and re-fires them after start cost / time it takes to attack, or it fires them itself when its time? Or break out into Attack Start and Attack commands.

- add distance to target position
- what if targeting a body part targets that position within the location, and hits whatever is in that position

Flee stops combat, combatants stay in location (for now)
Every target within a location can have an x,y,z position
- If a target doesn't have the alive property (is an activator) and health reaches 0, instead of dying, the activator/item is destroyed

### Crafting

- Cooking / recipes
  - Burn if not enough skill (first warn), consume ingredients
  - Gain xp for cooking
- Current manner of matching recipes with ingredients has a possible bug that the order of ingredients in an inventory could determine whether a recipe could be used or not - write a test and fix it


#### Combined Items
Items can be combined / crafted together
- For each ingredient in a recipe it can include a bodypart/location that the ingredient will be equipped to in the constructed item
- Items can have bodyparts/locations with 'equipped' items 
- An item’s combined properties include the adjustments from the sub-equiped items
- Maybe crafted item has taglist of adjusted stats, and only those stats are changed by equipped items?
- Items’ bodies have body parts with the different materials of an item (a hatchet has a wooden handle and metal head)

Ex: Hilt + blade = sword. Iron blade gives 2 attack to sword, but steel blade gives 3 etc

Ex2: Leather gauntlet can be crafted into casting gauntlet. Casting gauntlet has location/slots for liner, gem, and decoration. Liner could have a steel mesh that ups defence or a fur liner that increases heat. In battle / targeting these sub locations are ignored and only used to build the item’s total properties

Ideas
- Fletching
- Tanning
- In depth combination of parts
- Properties transfer from crafted ingredients to final weapon?

### Interaction

- Should check if source can interact, not be hardcoded to player
  - Interact
  - eat Food
  - no use found
  - ChopWood
  
  
#### Look/Examine
Skill check based on
- Perception: the current level of the skill
- Atmosphere can decrease perception (night, water, caves, etc)
- Clarity is perception x 10
- Stealth level of the object being examined
    - Stealth is increased by
    - Sneak level
    - Size of object
    - Items equipped
    - Effects 
- Conditional stealth effects: black adds stealth at night, but not day. Royal gown adds stealth at a nobility party but not out in the woods
- The difficulty of the examine task

Examine is successful if Clarity >= task difficulty * stealth level
- When using perception on self, stealth level is always 1


Task | Base difficulty
--- | ---
View targets | 1
View anatomy | 5
View equipped items | 3
View effects | 7
View hidden properties | 10


### Inventory

- Item could fit in bag weight but then bag be too heavy for character to move
- Looking at a creature will say what the creature has equipped (is wearing)
- Pickup/Place commands have optional count for item stacks

Item Ideas

- Powerful sword that boosts all stats but drains morality. Boost equivalent to how good you are. Turns into drains if morality becomes negative.

### General UI

- preferences for what messages show
- Possible CLI Tools
    - [Clink](http://mridgers.github.io/clink/)
    - [Picocli](https://github.com/remkop/picocli)
    - [JLine](https://jline.github.io/)


Bash completion
- https://iridakos.com/tutorials/2018/03/01/bash-programmable-completion-tutorial.html
- Dynamic complete?
- Could be seperate from main app

### Locations

#### Atmospheric Effects
- Attached to location
- Atmospheres that add tags and effects to everything in that location.
- Shallow water, deep water, under water, have effects, based on swimming, etc. Swimming is skill based on agility
- Atmospheric effects like fog and water, cast words can interact with them
- Create climates for locations to extend?

Temperature
- Range from -10 to 10 on average, though temp could exceed these extremes
- -10 is freezing point of water
- 10 is boiling / spontaneous combustion
- Locations have a temperature depending on location properties + current effects, burnining fires, snowstorms etc
- Player has own temperature rating that is the location temp + effects, equipped items, etc

Gamestate query for what weather it is?
- Use query to make thunderstorms more likely/only happen after heavy rain

Figure out how to specify if json is replacing (default) or appending

Hard code weather effect length to same cadence as weather tick (in same place), no need to specifiy effect length in json


Weather effects
Activate every 10 game ticks 
Have trigger conditions that dictate if they should fire
Target Conditions to determine who they hit? (Ex lightning should only strike one target)
Target condition: takes a list of targets and selects one or more (body part as well?)
Re-use for AI


Extract location bases
overrides keyword: any map can have overrides: []. Any key in the list overrides instead of appends
Converter tests?

Make the light level of outside daytime default to 10?

Make wheat field description based on light level, not time of day

#### Heat

Heat / light effects have an ‘emit’ effect that have a distance and strength: the emit effect casts heat/light etc to things within range
If a target is flamable and it takes more fire damage than it has fireDefense, it starts burning
Fire emits light in a radius from itself
Fire emits heat in a radius from iteself
If a target is not on fire, it takes 1 fire damage for every 10 heat it has

How to handle effects that do aoe more effects? (Fire emits heat, light)
- Specific manager?
- Specific kind of effect that emits conditions?

#### Gravity

Targets in a scene can have supportParent
GravityListener: Any target that doesn’t have a supportParent and has a position of Z > 0 should fall, unless it has the NoFall tag
On Scope Remove, check any target that previously had that support Parent. Remove the parent and let the item fall
A falling item should fall the the scene’s lower z boundary. This is 0 by default unless there is a below connection
Double check that on move to a boundary changes scene - make this work for all targets
When things fall to the lower boundary, the move listener should naturally move them to the scene below


#### Other

Query Based effects

- Storming: if outside + random chance
- Locations should have tags
- Dynamic description for location based on effects

Can body parts have actual locations (tree branches with apples etc)? Those locations could have targets with bodies / this could make it recursive
- Possibly good for towns/houses etc
- assume no for now but once all this done maybe...

- locations can dictate position of targets
- interacting with objects moves you to their position

#### Persistence
- Tree continues burning even after leaving it and coming back (Persistence within session)
  - When the location is saved it should store the 'last tic timer' and then compare to the current timer to know number of tics passed
- Scope Manager needs to do 'garbage collection' and have a 'max loaded locations' in memory etc

- Use time of day to do dynamic descriptions of locations
- Location Targets can take item names to spawn with (for containers etc)

Still need to persist
- battle

session stats save time

Optimizations
- Diff against base objects?
- Don't write 0s, nulls, falses, default values?

scopes have an age and an age reset
debug command to check /toggle if a cell should reset

In order  to get more ‘save slots’ / characters, either unlock characters or have a kid


### Magic

The point of words of power / magic is to increase world immersion (elements should interact and be used to solve situations), and to help the player feel specialized.

Skill is based on type (water, air, earth, fire)
Words of power learned indiviually / unlocked from quests and artifacts. Some words of power require a minimum level to use.
Same spells scale up in potency as their skill increases

Equippable gauntlets that help words from a certain element

Casting a spell can require skill checks in multiple skills (for spells like smoke, thunderstorm)

Atmospheric effects should effect spell cost / power
- Super hot, buff fire
- Super windy, buff wind
- Underwater, buff water
- In a cave, buff earth
- In a dense forest / cave, nerf air
- Spell that makes things more windy etc

**Upgrades**

Some spell types have an upgrade subtype that is a more powerful or unique subtype of that type. Artifiacts (wind amulet, vine gloves etc) allow for sub-type words to be used (Ice Heart Amulet allows for frost words to be used with the water skill. Lightning is the upgrade of fire. These spells should require a very high level in the base type.

**Combinations**

Require high level in both skills, though not as high as the 'upgrade' subtypes. (Could be a consequence of two effects mixing)

Most spells apply their type’s tag/condition/element to the target. Use flame on a log to light it; use water burst to put the flame out.

#### Spells
Type | Name | Description | Effects | Params | Cost
---| --- | --- | --- | --- | --- 
Water |  Heal | Recover Health | | Target, Amount |  Can use up to max focus/or max skill level, (whichever is lower)
Water |  Poison | Take damage over time | | Target, damage per tick, number of ticks | Damager per tick * number of ticks
Water | Jet | Burst of water that does one time damage to all bodyparts of one or more targets | Wet | Targets, damage amount | Damage * number of targets
Air | Push | Push the target backwards | Air Blasted | Target, Distance | Uses very few action points  
Air | Pull | Pull the target closer | Air Blasted | Target, Distance | Uses very few action points  
Air | Adrenaline | Increase action point gain. Better the more dexterity and the higher the wind skill. Hurt by encumberance etc
Earth | Rooted | Defense increased by level encumbered | Encased | Target, Amount, Duration | Amount only. Duration does not increase cost
Earth | Rock | Small size can be rapidly fired while larger sizes do more than linearly more damage, can cause stun, and take longer to fire. 100% crush damage (no magic damage) | | Target, size (1-3)
Fire | Candle | Adds an effect that drains focus each tick but increases light level by spell strength | Candle Effect | Light Level | Initial cost (off/on) is free, effect drains over time
Fire | Ember | Fire damage immunity | | Target, length of time immune, percent immunity | time * percent immunity. Max of focus/fire skill 
Fire | Flame Spout | High damage short range attack that leaves the target burning. Burns caster a proportional amount of damage (minus immunity). Does more damage if target is already burning | Burning | Target, Amount
Lightning | Shock | Shock target and chain to nearby targets. Caster recieves a portion of the attack | Shocked | Target, Amount
Ice | Freeze | Encase close-range target in ice | Frozen | Target, Duration
Ice | Shard | Ranged attack with high stab damage | | Target, Damage Amount
Stone | Crush | Do a large amount of crush damage. If target is encased, triple the damage and remove encased
Smoke | Confuse | Reduce target perception
Rain | Syphon | Transfer health from first to second target
Storm |  Thunderstorm | Summon thunderstorm effect on current location. Lighting has a 10% chance of striking a random target (any target in scene) every battle tick


Quake with 4 levels, 1 being passive

#### Misc

- Casting gauntlets that let you insert a gem/artifact for better casting.
- Staves give you more range to your attacks
- Leveling up gives more range?

On Burn listener
- Triggers on take burn damage
- % chance (based on burn strength?
- Add another burn condition for duration 3, 1 less strength than current effect
- Reciever is either current or neighbor body part
- Receiever must be flamable tage + has firehealth left
- Fire burns on single body parts, can spread etc? Tinder box does just 1 body part, 1 point of damage, but can spread
- Tinder box should not be stronger/burn longer than basic cast fire spell? 


### Quests

A Simple Pie
- Repeatable events for when items dropped
- On quest stage updated:  In tutorial it fires on each pickup item, if all items picked up then next quest section

Other
- First time hints / build out tutorial
- Quest event has array of help sentences?

### Stats

- Stats have skills that are improved by XP, attributes that improve through level up points, and derived stats like health
- Or stats improve by use, attributes are like stat categories and improve when their stats improve, properties are derived from attributes (health, endurance, etc)
- Each skill levelup adds 1 xp to attribute

Resting restores 10% of stamina and focus per hour
Traveling uses 1 stamina per AREA traveled (distance being from the scale class, an AREA being 10 HUMANs)

Property stats max min?
- how do we prevent firehealth from draining past 0 when target doesn’t have fire health

Endurance vs stamina?

Attribute | Derived Stat | Example Stats/Activities
--- | --- | ---
Dexterity | AP Regen | Crafting, jumping, climbing
Endurance | Stamina | Using stamina
Fitness | Health | Taking damage, eating food
Strength | Carry Weight | Attack skills, traveling with high encumbrance
Wisdom | Focus | Exploration

#### Skill Ideas
Exploration
  - Traveling to new locations, Finding things through search

Fitness
 - affected by eating unhealthy food etc


dance command
dance <type of dance>
- with better charisma / looks can affect people's moods, or make them angry
- learn different dances with possible effects

 
#### Experience adder
- Event(skill, amount)
- Only on event success
- Each formula should always give generous enough xp that a player should quickly level up to the point where they don’t get xp for doing the same task
- A task should give xp for less than the first 10 times it is done

Climb effort = min(base difficulty of object climbed, actual difficulty per player skill + encumbrance) * distance

Spell effort = spell difficulty = 1f - (spell requirement level / player magic level)
Spell difficulty * total cost?   Maybe only give xp if over 50%?



#### Progression

- Skills/commands unlock based on attribute level
- Show only unlocked commands in help, etc
- Create cheat/debug commands to level up etc

#### Morality

Morality/infamy system
- Morality based on what the player does good or bad, regardless of being noticed/caught
- Fame/Infamy based on deeds that you’ve done. The more witnessed/bigger deal the more fame/infamy
- Morality is carried by the player
- Fame is based on value for current location + an average of all other locations


#### Flaw system:

On a new game you have to pick a flaw (long term effect on one of your attributes/skills, etc). Eventually get it healed through an oracle?

When creating a character you can accept flaws/perks. Each flaw gives 1 perk point,  each perk  uses 1 point. There are mutually exclusive flaws/perks. You can’t take ugly (-10 charisma) and  handsome (+ 11 charisma). Flaws usually are a -10 in a skill, while perks are +11?

Stat | Perk | Flaw
--- | --- | ---
Charisma | Handsome | Ugly
Stamina | Young | Old

### Story

* Main story about npc realizing they are inside a construct
* Realizes that maybe there are other verbs that he’s not capable of thinking of. (NPCs would never think to skin the bark of a tree because that’s not in the game, etc)
* Magic is based on the understanding of the construct


Antagonist
- female
- Realises world is fake/computer construct
- Make player attached to antagonist / create friendship
- Over time she turns evil / believes she needs to destroy the world because it is fake
- Make conflict between understanding her motive and needing to defeat her to save the world

The language of min
Killer’s Haven - town where those who have accidently killed someone flee to for protection


#### Races

- New characters are locked in the ‘time frost’ and can be unlocked to be playable
- Encounter time frost frozen character before you can unlock them. Later gain unlock and new character, and know you can go back to get the first one.
- Certain quests unlock the ability to start a new character as a different race in the same world

Lentil 
- best smiths, can forge organic metal into special lentil weapons
- Can be planted, where self heals on turn but can’t move

Raku
- Can breathe under water
- Strong with water magic and healing

Vix 
- can climb really well, weild razor ropes, disliked by other races

### Dialogue

Dialogue currently only supports Queries

Say/speak to - choose target
Go into isSpeaking
Any input while isSpeaking goes to speak command
listof(exit, goodbye, quit) etc stops isSpeaking
All other commands parsed as speaking

Response request
Can we break it out so it takes a question string and list of answers
Or have it display the list of answers so that they are seperate from the question being asked?


Test speak command against a rock that glows when spoken to


### Travel

- Make starting a journey event based / handle replacing an existing event
- Maybe journey mode for travel and climb, progress events that can succeed, fail, or spawn other events

#### Climbing
- You can jump down if a location is below you, you'll take damage based on your agility + the distance to fall (jump down locations added in link)
- Burning the apple tree branches should make the user fall

Every body part has either a material or a roughness and an angle. Used for determining climb difficulty
Every body part has own health?
A material has a hardness
Fall damage based on defense + hardness of body part landed on
Fall picks random body part, higher agility + perks make you land on feet
Landing on feet takes less damage
Cushoned armor takes less damage than hard (steel) armor?
Weight of armor gives more damage

armor can have a grip stat that helps with climbing

- Materials are defined in their own file and have properties
- A bodypart node can have an exit location node. <- these are passed in by the parent location node. The lowest exit node is used for falling.
- Rework climbing so that you look at a target’s body nodes and then climbing direction is based on the node connections and difficulty is based on the material properties (slippery, rough, smooth, etc). Distance from lowest node = height for fall damage etc.

Materials have a name and properties
Material has properties (hardness, cushion, tags etc)


Jump
Calc distance to target. X dist + positive y dist. If y is negative, that subtracts from jump distance
Calc possible fall damage only based on negative y



### Validation tools

- Separate from test suite
- Check for duplicate names (across items, recipes, and activators)
- Check all targets reference valid events etc (triggers and trigger events)
- Check all activator targets reference actual items / locations etc
- Check all behavior receipies have params that match their assigned behaviors
- Requiring default params breaks everything since params don't override other params, have to remove param satisfied test > is there another way to check variables at compile time?


#### Command ideas
- Search - skill based, finds scope that's hidden
- Look (examine) object for its description
- Sheath / unsheath command?
- Hold Command?
- Emotive actions (dance, smile, frown) that contextually affect npc relationships

F flag forces without confirmation
S flag does silent

some commands can be discovered just by saying the name
unlocked commands are saved at the game level so multiple characters can use them


### Misc / Unsorted
- Readable behavior / item
- Be consistent. End all statements with periods.
- Remove hatchet and apple from starting inventory and place them in world etc
- Inventory carrying space

Attack command groups 0 vs 1 - make tests
 - Mock Player?


add stat categories?
add option to travel silently (just 'you travel to tree')


move (position) within a location command

chop apple throws exception

not properly to-stringing something?
use stairs should climb them

push / pull is not honoring a specific distance 


Debug
ignore encumberance?


cast command - pass args from all groups other than targets (take args after targets if they are there)


jump to vector
- can't jump beyond strength (encumberance, height, distance)

dismount - give location of parent
jump - give location of parent


clearly Telegraph multi tiered goals

Multi-event listeners?


Stealth actions / some actions have stealth rating / may be executed (even if hostile) and go unnoticed if stealth is high enough


Create a data test for spell costs to narrow in on a good formula

‘Human’ npcs should have varying levels. From level 3 peasonts to level 30 mercenaries

On game start, the oracle / antagonist tells the player to come talk to them, starts them on the main quest when you finally do talk with them.

Quests can be adaptable by giving quest triggers an alias to speak through. Since there is no animation / voiced dialogue, lines can be shuffled to backup characters etc.


trigger conditions have a priority. Of all conditions that meet their condition, the one with the highest priority is executed

Ai has two properties, Ai state and ai state time

debug mode can reset managers


Bad guy works to destroy world. Null pointers crash game and are called seams. At one point bad guy does something and throws a fake error. Crash to desktop and when the player restarts the game, the bad guy makes mention of the crash. Null pointers mean that we have reached a seam. We have touched the void

Bad guy recognizes the player and says you're really just an avatar for some diety creeping into our world

Debug setting ‘random: off’ to make random chance always succeed (for battle tests etc)


allow increments stat to take a minimum and maximum

Do a kill rats quest, where the rats have already been killed buy another adventurer

Ice skates: overtime you get new tools that allow you to explore new areas

Make AI run not only during battle, but give it an on-tick listener where it can integrate game state and decide to do something. (And start with it allowing player controlled ai to make a choice)
Only evaluate AI for current location
Maybe all command parsing come from AI and if not in battle / command is not an on-tick command, just cycle next ai etc.


Quest for casting gauntlet. 
Gauntlet give 10 + magic. 
Combined gem with glove. 
A bunch of chests, 1 / glove. 
Choosing one chest deletes all chests



Validation tools that:
- Check for duplicate names (across items and activators)
- Check all targets reference valid events etc (triggers and trigger events)

properties have max/min value? Burnhealth going below 0
fold body parts into locations soon!?
how do we configure auto load?

Property aggregator that gets stat + all child stats?
sum properties takes a list of properties and sums them
Has hook for child prop update
Used for composed items?


Make named condition sets > point at one name / reuse a list of conditions that must be true

Args test for returning the two empty lists - make sure it ends up just being one empty list
