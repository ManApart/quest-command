Commands simply parse / understand user input and then create events
Managers subscribe to events, update the gamestate and print to the console


avoid all state in commands, allow state to be handled by events and gamestate

an open world rpg with intense levels of interact, experienced through the command prompt

## TODO
* find a way to allow auto complete with tab
* some locations can only be accessed from other locations etc
* Story manager listens to all events, compares plot point criteria to each event to see if plot point should run
* Group commands in help manual
* climb down
* Scope manager don't include inventory
* Skill check for climb
* Partial match for targets. If a single word matches, use it (tree vs apple tree)


Validation tools that:
Check for duplicate names (across items and activators)
Check for duplicate command aliases
Check all targets reference valid events etc (triggers and trigger events)




manic mansion SCUM
ducktype
context free gramer, tokenizers, lexers, (yacc, lex)


Command ideas
* Search
* Look (examine) object for its description