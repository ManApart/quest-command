# Quest Command

An open world rpg with intense levels of interact, experienced through the command prompt

## Planning and Ideas

While the readme should cover how to build and run the game, as well with information on how the game works, these notes are the design docs of the game and should frequently change and be deleted (and changed to readme documentation) once the feature is implemented.

### AI

Rat AI
  - only attack with low aim
  - Approach dagger range if not already in dagger range

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
- Target has a soul and a body
- Items (targets) have bodies with no attach points
- Items’ bodies have body parts with the different materials of an item (a hatchet has a wooden handle and metal head)
- Target’s have a property (alive) if they are creatures etc
- If a target doesn't have the alive property (is an activator) and health reaches 0, instead of dying, the activator/item is destroyed

Spacial Nodes
- Location Node extends Spacial node and has a location
- Body Node extends Spacial node and has a body part
- Combat uses Body Nodes for aiming, instead of body part having an arbitrary direction

Climbing
- Body part has a material
- Materials are defined in their own file and have properties
- A bodypart node can have an exit location node. <- these are passed in by the parent location node. The lowest exit node is used for falling.
- Rework climbing so that you look at a target’s body nodes and then climbing direction is based on the node connections and difficulty is based on the material properties (slippery, rough, smooth, etc). Distance from lowest node = height for fall damage etc.


### Combat

#### Thoughts and Description

Battle commands (attack, defend etc) allow a hand to be specified or default to the hand that has the weapon with the most damage etc for that skill

Battles take place at different distances (knife, sword, lance, bow). Combatants can step closer or further back during their turn.

Each attack is given a target position: (high-left, mid-right etc). Combatants can use dodge (jump, duck, left, right) to temporarily adjust their position. They can also use block to block a specific position (the shield passively blocks the slot it's equipped to).

When an attack lands it picks a body part from the target position. If there are no body parts in the target position, it looks for one from any of the adjacent slots. Full damage is done to a slot at the attack target position, and half damage is done to a slot in adjacent positions.

Damage is assessed by getting the slots at a specific position and then working through each layer of equipped armor on that slot. Each layer subtracts its defense from total damage done. The leftover damage detracts the combatant's health.

Armour has critical percents. A critical is a hole in the armour where armor damage is ignored.

Balanced weapons are things like swords and daggers. Weighted weapons are things like maces, axes, and hammers.

**Battle Skills**
- Archery
- Block
- Dodge
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

Maybe attack listeners delegate to battle to be told if to execute or not. Battle either stores them and re-fires them after start cost / time it takes to attack, or it fires them itself when its time? Or break out into Attack Start and Attack commands.

- add distance to target position


### Crafting

- Cooking / recipes
  - Burn if not enough skill (first warn), consume ingredients
  - Gain xp for cooking
- Current manner of matching recipes with ingredients has a possible bug that the order of ingredients in an inventory could determine whether a recipe could be used or not - write a test and fix it

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

- Atmospheres that add tags and effects to everything in that location.
- Shallow water, deep water, under water, have effects, based on swimming, etc. Swimming is skill based on agility
- Time of day affect description, night time make perception go down.

#### Persistence
- Tree continues burning even after leaving it and coming back (Persistence within session)
  - When the location is saved it should store the 'last tic timer' and then compare to the current timer to know number of tics passed
- Persistence should happen across sessions.
- Scope Manager needs to do 'garbage collection' and have a 'max loaded locations' in memory etc

- Use time of day to do dynamic descriptions of locations
- Location Targets can take item names to spawn with (for containers etc)

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

Skill Ideas
- Exploration
  - Traveling to new locations, Finding things through search


Fitness, which is affected by eating unhealthy food etc

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

### Dialogue

Dialogue currently only supports Queries


### Travel

- Make starting a journey event based / handle replacing an existing event
- Maybe journey mode for travel and climb, progress events that can succeed, fail, or spawn other events

#### Climbing
- You can jump down if a location is below you, you'll take damage based on your agility + the distance to fall (jump down locations added in link)
- Convert climbing paths to generic paths with directions dictating next path instead of up/down only?
- Burning the apple tree branches should make the user fall
- Climb paths should be capable of one step that is both top and bottom (for stairs and ladders)

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




-----------------------------

location node exits
On arrive, add connections to target/body parts if they exist
If climable, climbing an activator changes your location node to the body part of the activator
if the body part has a connection, send player to connected location
on arrive to connected location, re-connect path back if connection had a target + part

body materials


double check parts are properly instanced through scope manager


Location link can optionally take a target and part (aka a target and location of it's body's network)
Location node can pass in an exit to a body part of a target


Can body parts have actual locations (tree branches with apples etc)? Those locations could have targets with bodies / this could make it recursive
- Possibly good for towns/houses etc
- assume no for now but once all this done maybe...

For now hardcoding commands and triggers to use the player's location parent for network. Eventually that should only be a default

climb look should include exits and dismounts


Climb Location
- Player has location and climb location
- Climb target: > set player climb location to target’s mount part
- Dismount - Remove climb location, possibly take fall damage
- Look operates differently if player has a climb location: it explains what they can climb etc
- Climb in a direction to climb the part in that direction, or climb a specific part, or default to the next part in a direction if there is only one part
- All climb parts have an exit node: either the input connection/exit or the location of the target itself.
- Exiting to the connected location will add a special ‘climb target’ that has the connected climb part, for climbing back down

Dismounting
- If part has exit connection, exit to that connection
- Spawn should give exit nodes to all lowest body parts + any inherited /connection nodes
- If Jump, take fall damage and place at climb target's parent location


Location Nodes:
```

{
    "name": "Kanbara Wall",
    "locationName": "Kanbara Wall",
    "parent": "Kanbara",
    "locations": [
        {
            "name": "Guard Post",
            "position": {
              "x": -1
            }
        },
        {
            "target": "Wall"
            "part": "Upper Wall"
            "connection": {
                "network": "Kanbara"
                "location": "Kanbara City"
                "target": "Wall"
                "part": "Upper Wall"
            }
        }
    ]
},
```

Bodies (location nodes)
```
{
    "name": "Head",
    "locationName": "Head",
    "parent": "Human",
    "locations": [
      {
        "name": "Chest",
        "position": {
          "x": -1
        }
      }
    ]
},
{
    "name": "Trunk",
    "locationName": "Trunk"
    "parent": "Tree"
    "locations": [
      {
        "name": "Branches",
        "position": {
          "x": -1
        }
      }
    ]
  },
```


Body Parts:
```
  {
    "name": "Head",
    "material": "flesh"
    "slots": [
      "Head Inner",
      "Head",
      "Head Outer"
    ]
  },
  {
    "name": "Canine Body",
    "material": "fur"
    "slots": [
      "Canine Body"
    ]
  },
    {
      "name": "Trunk",
      "material": "wood"
    },

```

Tests:

- Startclimbing only executes for climable property