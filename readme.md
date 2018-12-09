# Quest Command

[![Build Status](https://travis-ci.org/ManApart/QuestCommand.svg?branch=master)](https://travis-ci.org/ManApart/QuestCommand)

An open world rpg with intense levels of interaction, experienced through the command prompt

## Building / Running

### Building

Run AppBuilder's main method. I use the ide. This generates files so they don't need to be done at runtime. This only needs to be re-run when you change certain files (like adding new commands, events, event listeners etc)

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

`w && use tinder on tree && use apple on tree`
`w && use tinder on tree && bag && pickup apple && bag && use apple on tree`
`travel tree && climb tree`
`travel an open field barren patch && equip hatchet && chop rat && slash rat`
`travel farmer's hut interior && cook Raw Poor Quality Meat on range`


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

### General Design Notes

Any time an activator adds a new triggered event, it needs to be added to the triggered event when statement

Location positions are always relative to their parent. The parent is always (0,0,0). If a location is compared with a location outside the parent, the parent locations are compared.

An event, command and listener should share a package. Event should end in Event, command in Command, and listener without a suffix, with the same main name. If a listener is player only etc, prefix it with player
Ex:
* LookCommand
* LookEvent
* Look
* PlayerLook

### Research
* manic mansion SCUM
* ducktype
* context free gramer, tokenizers, lexers, (yacc, lex)

## Game Systems Explanation

Below are explanations of how the game works today. See `notes.md` for thoughts and ideas on systems that have not been implemented yet.

### Triggered Events

See the Triggered Event class to see what events can be used, and what params they can accept

### Story Events


#### Querying Game State

A Query object is used to evaluate a game value against a given value to see if the event should be executed. It contains the following fields

* Property to query on
* Params passed to that query
* The operator to evaluate `>, <, >=, <=, =, !=`
* The value to compare against

See the XXX class to see a list of queryable values and their parameters

### Locations

**Locations**
Locations describe an area and contain

* A unique name (Required)
* A description
* A list of items
* A list of activators
* A list of creatures

**Location Paths**
Location Paths describe the relationship between locations. They contain

* A unique name (Required)
* A unique name of the location it is adjacent to(Required)
  * For each attached location, optionally a position relative to the current location
  * When parsed, the attached location will get a reference back to the current location with the proper position so that they can be referenced either way
  * The attached location will not get a link back if it already has an explicit link or the link is marked with the `oneWay` keyword.
* A parent name - used only in descriptions (ex: Blue Cave is part of Kanbara Wilds)
* Restricted - locations are visible but can't be traveled to (for things like climbing trees or opening doors)

