

Commands simply parse / understand user input and then create events
Managers subscribe to events, update the gamestate and print to the console


avoid all state in commands, allow state to be handled by events and gamestate

an open world rpg with intense levels of interactivity, experienced through the command prompt

## TODO
* find a way to allow auto complete with tab
* some locations can only be accessed from other locations etc
* use dagger on self could test use interface and drop health (even if it's kinda dark)
* Story manager listens to all events, compares plot point criteria to each event to see if plot point should run
