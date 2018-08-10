# Quest Command

An open world rpg with intense levels of interact, experienced through the command prompt

## Design

### Goals

Create a world more interactive than Skyrim by trading presentation layer for higher levels of interaction.

### Design Pillars

* Architecture is flat and increases in complexity relative to project size in as small of increments as possible.
* Classes are small, decoupled, and do just one thing
* Dependency and coupling are kept to a minimum

### Design Principles

* Commands simply parse / understand user input and then create events
* Commands do not handle or change state
* Commands should be unknown to game state, events, and logic
* All intents and actions are created through events
* Listeners subscribe to individual events, update gamestate and print to console.

### Design Notes

Any time an activator adds a new triggered event, it needs to be added to the triggered event when statement

Location positions are always relative to their parent. The parent is always (0,0,0). If a location is compared with a location outside the parent, the parent locations are compared.

An event, command and listener should share a package. Event should end in Event, command in Command, and listener without a suffix, with the same main name. If a listener is player only etc, prefix it with player 
Ex: 
* LookCommand
* LookEvent
* Look
* PlayerLook

## Planning and Ideas

### Research
* manic mansion SCUM
* ducktype
* context free gramer, tokenizers, lexers, (yacc, lex)


### Skill ideas
* Barehanded
* Right and Left handed
* Short sword, sword, axe, lance

### Command ideas
* Search - skill based, finds scope that's hidden
* Look (examine) object for its description
  * Look during climb says the options you can go
  * Prevent travel during climb
  * Prevent using items etc during climb
* Dismount - stop climbing if you are at the top or bottom of a climb (no damage)
* Battle Commands (each can take a direction), (default to item in right hand, option to add left)
  * Slash - grazing damage but generally fast (hand slaps)
  * Chop - most damage, slowest
  * Stab - 
  * Crush
  * Dodge
  * Block 
  * Step (forward, back) (ranges: knife, sword, lance, bow)
* Sheath / unsheath command?
* Hold Command?
  
  **Battle**
  
 In battle, a creature can act when it attains 100 action points. Action points are increased by the creature's agility, alternating between the two creatures. When 100 ap are achieved, the creature gets a turn and the ap are reset to 0. During a turn, a player can take any normal command / action, but will generally choose a battle action. The player can equip either hand, or use a hand. Each of the battle commands default's to the right hand, but can be preferenced with right or left to indicate a hand. If block is chosen without a hand preference, it will default to the hand that has a shield, is armed, or the right hand. 
 
 Block can be high, low or medium. If the player blocks high and the opponent strikes high, the shield will take all damagage. If the opponent strikes medium (one height away from block area), the shield takes medium damage (and low, the sheild would block no damage).
 
 Weapons have different reach and the player may need to step closer, or step back to avoid a blow.

Agility affects how quickly someone can attack, strength how much the attack does, perception how likely to hit, etc

## TODO

### Long Term

* find a way to allow auto complete with tab
* Story manager listens to all events, compares plot point criteria to each event to see if plot point should run
* Better package organization for actions
* Packages are still a mess
* Convert climbing paths to generic paths with directions dictating next path instead of up/down only
* Make starting a journey event based / handle replacing an existing event
* Redo location system so it's not so nested?
* Stats have skills that are improved by XP, attributes that improve through level up points, and derived stats like health
* First time hints
* preferences for what messages show
* Give json inherit keywords. Spawning grabs all properties etc from inherited
  * Inheritable has properties and behavior recipes and params
* Behaviors have default keywords (itemName, etc) that can be used as variables
* Maybe journey mode for travel and climb, progress events that can succeed, fail, or spawn other events
* Cooking / recipes


### Short Term

* Inventory carrying space
* you can jump down if a location is below you, you'll take damage based on your agility + the distance to fall
* Break scope manager and item manager listeners into own classes
* Burning the apple tree branches should make the user fall


### Validation tools

* Seperate from test suite
* Check for duplicate names (across items and activators)
* Check for duplicate command aliases
* Check all targets reference valid events etc (triggers and trigger events)
* Check all behavior receipies have params that match their assigned behaviors




  

Flow of param overrides (left overrides right)
Item Params > inheritable params > Behavior Params/Properties > Conditions/events

