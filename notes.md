# Quest Command

An open world rpg with intense levels of interact, experienced through the command prompt

## Planning and Ideas

While the readme should cover how to build and run the game, as well with information on how the game works, these notes are the design docs of the game and should frequently change and be deleted (and changed to readme documentation) once the feature is implemented.

### AI

Rat AI
  - only attack with low aim
  - Approach dagger range if not already in dagger range
  
Combat training dummy
- Player controlled ai
- Used to test fights


### Architecture

- Better package organization for actions
- Packages are still a mess
- Set target copy's location to spawned location
- Reset vs Reload. Reset resets game state, reload reloads json?
- Flow of param overrides (left overrides right)
  - Item Params > inheritable params > Behavior Params/Properties > Conditions/events
- Break scope manager and item manager listeners into own classes
- make evaluate and execute return true/ a number so caller can be aware if behavior etc found
- Managers etc should return copies of items so that the parsed ones are immutable
- Create a git wiki and move readme information to more sorted format?
- Validate valid recipe ingredients and results
- make target class instead of interface?
- Break triggered event class out into multiple classes
- delete existing generated files on buildData so renamed files don't leave artifacts
- It'd be cool to eventually make an android app fork that allows tap menus to pic commands: Starts with categories, then uses the ResponseRequest to generate suggestions etc
- double check body parts are properly instanced through scope manager
- For now hardcoding commands and triggers to use the player's location parent for network. Eventually that should only be a default

Json
- Json inherits from other files in same folder?
- Inherit object that's common to item and activator (burnable)?

Paramitization
- paramatize targets on spawn events
- delete behavior recipe / rework for global params
- replace non-parameratized $keys?
- paramatize numbers/boolean like weight?
- follow param pattern with creatures
- Eventually param locations and location nodes. Ex: Location Node windmill can pass grain bin node location down to chute.

#### Creature / Activator Re-work

Turn creatures, items, and activators into targets
- Items’ bodies have body parts with the different materials of an item (a hatchet has a wooden handle and metal head)
- Target’s have a property (alive) if they are creatures etc
- If a target doesn't have the alive property (is an activator) and health reaches 0, instead of dying, the activator/item is destroyed

Spacial Nodes
- Combat uses Body Nodes for aiming, instead of body part having an arbitrary direction

Climbing
- Body part has a material
- Materials are defined in their own file and have properties
- A bodypart node can have an exit location node. <- these are passed in by the parent location node. The lowest exit node is used for falling.
- Rework climbing so that you look at a target’s body nodes and then climbing direction is based on the node connections and difficulty is based on the material properties (slippery, rough, smooth, etc). Distance from lowest node = height for fall damage etc.

Materials have a name and properties
Effects stored in soul but can have reference to affected target parts?

Are body parts full on targets?
That would make crafting easier as it would literally be the combination of body parts
They at least need properties for health?
Material has proeprties (hardness, cushion, tags etc)



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


#### Startup

Speed up startup by doing reflection and then writing out a hardcoded class with all the class references. Best case use library etc. Worst case write script to custom write class out (should be mostly hard coded stuff). This class should work through dependency injection and should be replacable for tests etc.

Spells should do the same type of reflection as commands. They should have an interface like commands and be dynamically read. The Spell Command (Syntax: Cast wind blast on self) recognizes the cast keyword, then passess it to the ‘spell parser’ which is just like the command parser (maybe re-use class) but parses out the spell, a list of args, and a target. The target and args are passed to that specific ‘spell-command’ which then builds all the events that should fire for that spell.




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

#### Body Location Rework

- Allow advance/ dodge commands to specify a distance?
- Give all weapons a range
- Create dagger base etc that can be inherited by all daggers


#### Other
- Boss that you fight by climbing, hitting, getting thrown off, taking fall damage, repeating
- Attack direction should use previous target
- Individual health for body parts, can't use parts that don't have health?

Maybe attack listeners delegate to battle to be told if to execute or not. Battle either stores them and re-fires them after start cost / time it takes to attack, or it fires them itself when its time? Or break out into Attack Start and Attack commands.

- add distance to target position


Flee stops combat, combatants stay in location (for now)
Every target within a location can have an x,y,z position


### Crafting

- Cooking / recipes
  - Burn if not enough skill (first warn), consume ingredients
  - Gain xp for cooking
- Current manner of matching recipes with ingredients has a possible bug that the order of ingredients in an inventory could determine whether a recipe could be used or not - write a test and fix it

Ideas
- Fletching
- Tanning
- In depth combination of parts
- Properties transfer from crafted ingredients to final weapon?


### Effects

#### Chemistry Engine

Whenever a new effect is applied, onFirstApply(newEffect, targetSoul) is called. For elements that have a relationship, the strengths are compared and the weaker effect is cleared / the element logic is applied.

Replace burn-health stat with burn-strength. On scene load, chemestry engine should give targets with certain tags the flamable effect. Wood gets a flammable effect with a strength of 10. Kindling gets a flammable effect with a strength of 1, etc. 
Based on tags, chemistry engine should give other effects (wet, earth etc). To light a tree (with flammable strength 10) on fire, the player needs to use an attack / element with a strength greater than 10. This is a change from burn health that can be drained.


#### Elements

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


#### Technical Structure

Hard code effect bases as interface / classes. Effect instances are generated by spells / quest events etc. They’re given a name to find the base, and then amount/duration. 

Effect Bases have
- name
- description
- statAffectType
- element
- amountType: percent or flat number
- damageType
- target: stat this effect targets

Effect Instances have
- amount
- duration (number of turns applied)
- elementStrength
- bodypart

**DamageType**

If the stat to affect is heatlh, this property should explain what type of damage is done to the health (so that it can be defended against

Energy is another type of damage (slash, crush etc). This is used for fire, water, air, lightning, etc.

**Element** 

Used for the chemestry engine. Fire, water, lightning, none, etc.

**ElementStrength**

How strong the element is for this effect. Usually similiar to cost formula. Used for the chemestry engine. (immutable)

**StatAffectTypes**

Type | Description
--- | ---
Drain | Each turn reduce current stat by this amount
Deplete | Only reduce the stat once (when first applied). When the effect is removed, recover the stat by the same amount
Boost| Only increase the stat once (when first applied. Reduce the stat by the same amount when the effect is removed
Recover| Each turn increase current stat by this amount
None | 

**Duration**

Will be commuted to 0 if an effect is cleared due to element strength. If 0/blank, there is no duration and the effect is permanent until cured.

If an effect is applied to a target that already has that effect’s clear-by effect, Both effects are canceled For instance if a target is wet and is given the burning tag, both effects are removed. Another example: a target is burning and then given the encased effect; both effects are canceled.

#### Example Effects

Once these are codified in Json, remove them from here

Name | Stat | Type | Element | Damage Type | Description
--- | --- | --- | --- | --- | ---
Burning | Health | Drain | Fire | Energy | Damage over time 
Wet | | | Water | | Slightly reduce agility 
Encased  | | | Earth | | Greatly reduce agility. Prevent movement. Greatly increases slash and stab defense. 
Air Blasted | | | Air| | Ends instantly but may clear other statuses
Shocked | | | Lightning | | Deplete 100 action points
Frozen | | | Ice | | Drain 100 action points
Stunned | | | None | | Deplete 50 action points. Immediately clears



#### Misc

Should effects be an effect group with an element and then child effects?



### Interaction

- Should check if source can interact, not be hardcoded to player
  - Interact
  - eat Food
  - no use found
  - ChopWood



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

Atmospheric Effects
- Attached to location
- Atmospheres that add tags and effects to everything in that location.
- Shallow water, deep water, under water, have effects, based on swimming, etc. Swimming is skill based on agility

Query Based effects
- Time of day affect description, night time make perception go down.
- Storming: if outside + random chance
- Locations should have tags
- Day night cycle
- Dynamic description for location based on effects

Can body parts have actual locations (tree branches with apples etc)? Those locations could have targets with bodies / this could make it recursive
- Possibly good for towns/houses etc
- assume no for now but once all this done maybe...

#### Persistence
- Tree continues burning even after leaving it and coming back (Persistence within session)
  - When the location is saved it should store the 'last tic timer' and then compare to the current timer to know number of tics passed
- Persistence should happen across sessions.
- Scope Manager needs to do 'garbage collection' and have a 'max loaded locations' in memory etc

- Use time of day to do dynamic descriptions of locations
- Location Targets can take item names to spawn with (for containers etc)


### Magic

The point of words of power / magic is to increase world immersion (elements should interact and be used to solve situations), and to help the player feel specialized.

Skill is based on type (water, air, earth, fire)
Words of power learned indiviually / unlocked from quests and artifacts. Some words of power require a minimum level to use.
Same spells scale up in potency as their skill increases

**Upgrades**

Some spell types have an upgrade subtype that is a more powerful or unique subtype of that type. Artifiacts (wind amulet, vine gloves etc) allow for sub-type words to be used (Ice Heart Amulet allows for frost words to be used with the water skill. Lightning is the upgrade of fire. These spells should require a very high level in the base type.

**Combinations**

Require high level in both skills, though not as high as the 'upgrade' subtypes. (Could be a consequence of two effects mixing)

Most spells apply their type’s tag/condition/element to the target. Use flame on a log to light it; use water burst to put the flame out.


#### Spell Types
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
Fire | Ember | Fire damage immunity | | Target, length of time immune, percent immunity | time * percent immunity. Max of focus/fire skill 
Fire | Flame Spout | High damage short range attack that leaves the target burning. Burns caster a proportional amount of damage (minus immunity). Does more damage if target is already burning | Burning | Target, Amount
Lightning | Shock | Shock target and chain to nearby targets. Caster recieves a portion of the attack | Shocked | Target, Amount
Ice | Freeze | Encase close-range target in ice | Frozen | Target, Duration
Ice | Shard | Ranged attack with high stab damage | | Target, Damage Amount
Stone | Crush | Do a large amount of crush damage. If target is encased, triple the damage and remove encased
Smoke | Confuse | Reduce target perception
Rain | Syphon | Transfer health from first to second target
Storm |  Thunderstorm | Summon thunderstorm effect on current location. Lighting has a 10% chance of striking a random target (any target in scene) every battle tick


#### Misc

- Casting gauntlets that let you insert a gem/artifact for better casting.
- Staves give you more range to your attacks
- Leveling up gives more range?



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


### Story

* Main story about npc realizing they are inside a construct
* Realizes that maybe there are other verbs that he’s not capable of thinking of. (NPCs would never think to skin the bark of a tree because that’s not in the game, etc)
* Magic is based on the understanding of the construct


#### Races


New characters are locked in the ‘time frost’ and can be unlocked to be playable
Encounter time frost frozen character before you can unlock them. Later gain unlock and new character, and know you can go back to get the first one.


Lentil 
- best smiths, can smith lentil weapons
- Can be planted, where self heals on turn but can’t move

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

Every body part has a material
Every body part has own health?
A material has a hardness
Fall damage based on defense + hardness of body part landed on
Fall picks random body part, higher agility + perks make you land on feet
Landing on feet takes less damage
Cushoned armor takes less damage than hard (steel) armor?
Weight of armor gives more damage


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

### Misc / Unsorted
- Readable behavior / item
- Be consistent. End all statements with periods.
- Remove hatchet and apple from starting inventory and place them in world etc
- Inventory carrying space

Certain quests unlock the ability to start a new character as a different race in the same world


Attack command groups 0 vs 1 - make tests
 - Mock Player?


add stat categories?
add option to travel silently (just 'you travel to tree')


##### Bodies

Object | Length
--- | ---
human | 10
fist range | 1
dagger range | 2
sword range | 5
axe range | 5
spear range | 7
bow range | 20

A attacks arm of B
B dodges
If A still in range of arm, still hits arm
if not, miss for now

slash should attack all objects within weapon range of root object

attack force override

Shield sizes (radius)
Traveling to a target puts you at 0,0,0
locations can dictate position of targets
interacting with objects moves you to their position
what if targeting a body part targets that position within the location, and hits whatever is in that position


position of a sublocation (body part) is it's position + the position of the parent



-------------

TODOS
Direction Parser