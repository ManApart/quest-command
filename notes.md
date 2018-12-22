# Quest Command

An open world rpg with intense levels of interact, experienced through the command prompt

## Planning and Ideas

While the readme should cover how to build and run the game, as well with information on how the game works, these notes are the design docs of the game and should frequently change and be deleted (and changed to readme documentation) once the feature is implemented.

### Architecture

- Better package organization for actions
- Packages are still a mess
- Make activators / items able to extend others (parse all, loop through each copying base and updating properties on each sub-item etc)
- Set activator copy's location to spawned location
- Reset vs Reload. Reset resets game state, reload reloads json?
- Flow of param overrides (left overrides right)
  - Item Params > inheritable params > Behavior Params/Properties > Conditions/events
- Break scope manager and item manager listeners into own classes
- make evaluate and execute return true/ a number so caller can be aware if behavior etc found
- Managers etc should return copies of items so that the parsed ones are immutable
- Create a git wiki and move readme information to more sorted format?
- Replace Inhertiable with pure json inheritance?
- Validate valid recipe ingredients and results

### Combat

#### Thoughts and Description
In battle, a creature can act when it attains 100 action points. Action points are recovered based on the creature's agility, alternating between the two creatures. When 100 ap are achieved, the creature gets a turn and the ap are reset to 0. During a turn, a player can take any normal command / action, but will generally choose a battle action. The player can equip either hand, or use a hand. Each of the battle commands default's to the right hand, but can be preferenced with right or left to indicate a hand. If block is chosen without a hand preference, it will default to the hand that has a shield, is armed, or the right hand.
 
Block can be high, low or medium. If the player blocks high and the opponent strikes high, the shield will take all damagage. If the opponent strikes medium (one height away from block area), the shield takes medium damage (and low, the sheild would block no damage).
 
Weapons have different reach and the player may need to step closer, or step back to avoid a blow.

Agility affects how quickly someone can attack, strength how much the attack does, perception how likely to hit, etc

Each Turn
- Increase combatants AP by their agility level until one person hits 100
- Each action takes AP. Bare fists take 1 ap, the bigger the weapon (size keyword) the more ap it takes
- Each action drains stamina. The heavier the weapon the more stamina drained
- Without enough stamina the combatant can’t do anything
- A Combatant can rest to recover stamina points

Attacking with heavier weapons does what?
- Costs more action points (instead of always resetting to 0 be based on weapon)?
- Drains stamina?
- Is only an issue if the weapon is heavy compared to your strength level?

Attacking different body parts do anything? What's the point of the attack direction?

#### Battle Skill ideas
- Barehanded
- Right and Left handed
- Short sword, sword, axe, lance

#### Battle Command Ideas
Battle Commands (each can take a direction), (default to item in right hand, option to add left)
- Slash - grazing damage but generally fast (hand slaps)
- Chop - most damage, slowest
- Stab -
- Crush
- Dodge
- Block
- Step (forward, back) (ranges: knife, sword, lance, bow)

### Crafting

- Cooking / recipes
 - Burn if not enough skill
 - Gain xp for cooking
- Slice apple
- Behavior / interact / recipe alignment. Use knife on apple same as slash apple, same as craft sliced apple
 - Effects should apply properties during their duration and then remove them afterwords (like 'burning' when on fire)
 - Range needs lit, goes from 'Range (unlit)' to 'Range (lit)'
 - Maybe like this:
  - knife used on apple gives apple 'sliced' tag
  - Sliced, Baked, Burnt, Cooked
  - Target has 'display name' that adds tag names to target??
  - Pie recipe takes apple (sliced)
  - Use fire on apple, becomes baked apple (tag), tag makes heal worth 1.5x when eaten ( behavior)
  - Pie should leave a pie tin once eaten > Behavior on Pie, when eaten drop tin
- Recipe action occurs if other actions don’t, finds available recipes
- If more than one recipe works display message so they can choose which recipe they want to craft
- Current manner of matching recipes with ingredients has a possible bug that the order of ingredients in an inventory could determine whether a recipe could be used or not - write a test and fix it




### Inventory

- Weapons have a type (property)
- Bag can hold x amount of weight, any type
- Character can only hold so much weight.
 - Item could fit in bag weight but then bag be too heavy for character to move
- Bags / armor can have equip slots for specific types. So a weapon holster can provide a slot but only for hatchets etc


### General UI

- find a way to allow auto complete with tab
- preferences for what messages show
- Possible CLI Tools
    - [Clink](http://mridgers.github.io/clink/)
    - [Picocli](https://github.com/remkop/picocli)
    - [JLine](https://jline.github.io/)

### Locations

- Location items take hashmap of item name and locationDescription string. Looking in a location will say ‘tinderbox on the range’
- Place the hatchet/tinderbox/apple in the scenes

#### Other
- Boss that you fight by climbing, hitting, getting thrown off, taking fall damage, repeating
- Attack direction should use previous target

#### Persistence
- Tree continues burning even after leaving it and coming back (Persistence within session)
 - When the location is saved it should store the 'last tic timer' and then compare to the current timer to know number of tics passed
- Persistence should happen across sessions.


### Quests

- Tutorial teach players how to pick things up, tell them to light the range, cook an apple pie with water, wheat, apple, tin etc
- On quest stage updated:  In tutorial it fires on each pickup item, if all items picked up then next quest section

Other
- First time hints / build out tutorial
- Quest event has array of help sentences

### Stats

- Stats have skills that are improved by XP, attributes that improve through level up points, and derived stats like health
- Or stats improve by use, attributes are like stat categories and improve when their stats improve, properties are derived from attributes (health, endurance, etc)
- Each skill levelup adds 1 xp to attribute

Possible Stats
- Strong
- Clever
- Agile
- Wise
- Enduring

### Story

* Main story about npc realizing they are inside a construct
* Realizes that maybe there are other verbs that he’s not capable of thinking of. (NPCs would never think to skin the bark of a tree because that’s not in the game, etc)
* Magic is based on the understanding of the construct


### Travel

- Redo move command to move to location by finding path is possible
- Make starting a journey event based / handle replacing an existing event
- Maybe journey mode for travel and climb, progress events that can succeed, fail, or spawn other events
- Update how routing works
 - Use some path finding to find nearest area by evaluating nodes out from center node
 - let map be used for route planning
- Make moving up/down restricted by default and when moving in that direction look for an object to climb or note that you could jump down

#### Climbing
- You can jump down if a location is below you, you'll take damage based on your agility + the distance to fall
- Look during climb says the options you can go
- Convert climbing paths to generic paths with directions dictating next path instead of up/down only?
- Burning the apple tree branches should make the user fall

### Validation tools

- Separate from test suite
- Check for duplicate names (across items, recipes, and activators)
- Check all targets reference valid events etc (triggers and trigger events)
- Check all activator targets reference actual items / locations etc
- Check all behavior receipies have params that match their assigned behaviors


### Misc / Unsorted
- Readable behavior / item
- Be consistent. End with periods or without them.
- Remove hatchet and apple from starting inventory and place them in world etc
- Directly cook recipe
- Inventory carrying space

#### Command ideas
- Search - skill based, finds scope that's hidden
- Look (examine) object for its description
- Sheath / unsheath command?
- Hold Command?










