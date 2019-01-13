# Quest Command

[![Build Status](https://travis-ci.org/ManApart/QuestCommand.svg?branch=master)](https://travis-ci.org/ManApart/QuestCommand)

An open world rpg with intense levels of interaction, experienced through the command prompt

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

`w && use tinder on tree && use apple on tree`
`w && use tinder on tree && bag && pickup apple && bag && use apple on tree`
`travel tree && climb tree`
`travel an open field barren patch && equip hatchet && chop rat && slash rat`
`travel farmer's hut interior && cook Raw Poor Quality Meat on range`


## Design

### Goals

Create a world more interactive than Skyrim by trading presentation layer for higher levels of interaction.

### Design Pillars

- Architecture is flat and increases in complexity relative to project size in as small of increments as possible.
- Classes are small, decoupled, and do just one thing
- Dependency and coupling are kept to a minimum
- Least effort possible for content creation
  - Content should be as concise as possible without losing clarity
  - Everything should have a reasonable default
  - Content creators should only need to specify what makes an item etc unique


### Design Principles

- Commands simply parse / understand user input and then create events
- Commands do not handle or change state
- Commands should be unknown to game state, events, and logic
- All intents and actions are created through events
- Listeners subscribe to individual events, update gamestate and print to console.

### General Design Notes

Any time an activator adds a new triggered event, it needs to be added to the triggered event when statement

Location positions are always relative to their parent. The parent is always (0,0,0). If a location is compared with a location outside the parent, the parent locations are compared.

An event, command and listener should share a package. Event should end in Event, command in Command, and listener without a suffix, with the same main name. If a listener is player only etc, prefix it with player
Ex:
- LookCommand
- LookEvent
- Look
- PlayerLook

### Research
- manic mansion SCUM
- ducktype
- context free gramer, tokenizers, lexers, (yacc, lex)

## Game Systems Explanation

Below are explanations of how the game works today. See `notes.md` for thoughts and ideas on systems that have not been implemented yet.


### Crafting

#### Recipes

Recipes take in a set of skills, properties for the required tool, and a set of ingredients. They can be triggered by explicitly crafting a recipe or by using an ingredient on a tool etc. Sliced Apple can be created by `craft sliced apple`, by `use dagger on apple` or by `slash apple` if the ingredients, tools, and skills are all present.

In the case of an Apple Pie, an Apple, Pie Tin and Dough are required. The Apple must have the tag `Sliced`.
```
"name": "Apple Pie",
"ingredients": [
  {
    "name": "Apple",
    "tags": "Sliced"
  },
  "Pie Tin",
  "Dough"
]
```


In the case of baked fruit, we don't know exactly what fruit we'll be given, because the ingredient doesn't have a name, but we do know it must have the `Fruit` and `Raw` tags.
```
"name": "Baked Fruit",
"ingredients": [
  {
    "tags": [
      "Fruit",
      "Raw"
    ]
  }
],
```

Recipes generally yield a single result: `"results": ["Apple Pie"]`, but they can also yield multiple items, like in the case of Dough: `"results": ["Dough","Bucket of Water"]`.

They can also add or remove tags to a passed in ingredient. In the case of baked fruit, the first passed in ingredient (referenced by `"id": 0`) is given the baked tag and loses the raw tag:
```
  "results": [
   {
     "id": 0,
     "tagsAdded": [
       "Baked"
     ],
     "tagsRemoved": [
       "Raw"
     ]
   }
 ]
 ```
 A new item can be created (using the `name` property instead of the `id` property) and have tags added / removed as well.


A couple things to note:
- All conditions for recipes are `AND`; all conditions must be met for a recipe to be used.
- In the above Baked Fruit example you could not re-bake the fruit because `Fruit` must be `Raw` and that tag is removed from the result.
- A recipe without any tool properties can be made without a specific tool.

### Inventory

Players can see their own inventory and the inventory of any target that has the `Container` tag.

They can take objects from any container that has the `Container` and `Open` keywords.

They can place items in a container if it has the `Container` and `Open` tags and if the container has remaining capacity. Remaining capacity is determined by a creature's current strength level, or the container's `Capacity` value. Containers may have a CanHold value: `"CanHold": "Dagger,Bow"`. If a container does not have a CanHold property than any type of item can be placed in it. If it does have the property, than only items that have at least one matching tag can be placed in the container.

### Triggered Events

See the Triggered Event class to see what events can be used, and what params they can accept

### Story Events

Quests are made up of Story Events. Each event has a stage. When the event runs the quest is automatically updated to be that stage. The first valid stage found will be executed.

Properties of note:

- Completed - Event has been completed
- Repeatable - Can be hit even if completed before
- AvailableAfter - After (`>=`) this stage this event can be hit if the conditions are met, even if that means skipping other stages. If blank available only when this is the next highest stage after current stage
- AvailableUntil - This event can be hit up until this stage (`<=`). If blank available until is just the next highest quest stage after this

Available after and available before default to the story event's stage, meaning the story event cannot be hit unless the event is the next stage in the quest.


### Trigger Conditions

Trigger Conditions evaluate when a looked for calling event is posted. It uses a combination of event params (values of the event variables) and queries to evaluate true or false.

```
"condition": {
  "callingEvent": "UseEvent",
  "eventParams": {
    "used": "$used"
  }
},
```
Trigger conditions get the current values of the event properties and can use them for evaluation. For example, the used event has values `source`, `used`, and `target`. The above condition will trigger on any `UseEvent` because it's checking that the `used` property of the event matches its own value (`$used`).


#### Querying Game State

A Query object is used to evaluate a game value against a given value to see if the event should be executed. It contains the following fields

- Property to query on
- Params passed to that query
- The operator to evaluate `>, <, >=, <=, =, !=`
- The value to compare against

See the `GameStateQuery` class to see a list of queryable values and their parameters.

### Locations

#### Locations
Locations describe an area and contain

- A unique name (Required)
- A description
- A list of items
- A list of activators
- A list of creatures

When specifying a list of targets (items, activators, creatures), you can give just their name, or their name and location within the location. This is just a string of flavor text used for the look command:
```
{
    "name": "Apple",
    "location": "at the base of the tree"
}
```

#### Location Paths
Location Paths describe the relationship between locations. They contain

- A unique name (Required)
- A unique name of the location it is adjacent to(Required)
  - For each attached location, optionally a position relative to the current location
  - When parsed, the attached location will get a reference back to the current location with the proper position so that they can be referenced either way
  - The attached location will not get a link back if it already has an explicit link or the link is marked with the `oneWay` keyword.
- A parent name - used only in descriptions (ex: Blue Cave is part of Kanbara Wilds)
- Restricted - locations are visible but can't be traveled to (for things like climbing trees or opening doors)

## Testing

If tests are running slow, make sure that json parsing and reflection are not happing as part of unit tests. An easy check is to place a debug point in `getImplementation` to catch any time a default implementation is being used in a unit test.
