Commands simply parse / understand user input and then create events
Managers subscribe to events, update the gamestate and print to the console


avoid all state in commands, allow state to be handled by events and gamestate

an open world rpg with intense levels of interact, experienced through the command prompt

## TODO
* find a way to allow auto complete with tab
* Story manager listens to all events, compares plot point criteria to each event to see if plot point should run
* Skill check for climb
* Better use command debug messages, refactor big if statement
  * If target not found, give error message, not just explain item
* Flamable Objects should be able to be set on fire
  * Open field (grass)
  * Apple Tree
* Tree should be scratched by dagger, cut down by axe
  * Cut down should turn into logs
* Apple should be roasted, roasted apple should be burnt
* Items should be equippable
* Inventory carrying space
* Better package organization for actions
* Jump down from location (goes to parent location)
* Use weapon on player not doing damage
* Stat minned / max hard coded for player. Convert to their own events (player death, any other creature death, stat maxed message for player only)

Validation tools that:
Check for duplicate names (across items and activators)
Check for duplicate command aliases
Check all targets reference valid events etc (triggers and trigger events)




manic mansion SCUM
ducktype
context free gramer, tokenizers, lexers, (yacc, lex)


Command ideas
* Search - skill based, finds scope that's hidden
* Look (examine) object for its description
* Battle Commands (each can take a direction), (default to item in right hand, option to add left)
  * Slash 
  * Chop
  * Stab
  * Dodge
  


