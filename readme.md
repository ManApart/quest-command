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
