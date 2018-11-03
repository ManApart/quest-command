# Quest Command

An open world rpg with intense levels of interact, experienced through the command prompt

## Building / Running

### Building

Run AppBuilder's main method. I use the ide. This generates files so they don't need to be done at runtime. This only needs to be re-run when you change certain files (like adding new commands, events, event listeners etc)

Run `gradlew build jar` to build a jar. This should be found in `QuestCommand/build/libs` (`quest-command-1.0-SNAPSHOT`)


### Running
Navigate to `QuestCommand/build/libs` and run `java -jar quest-command-1.0-SNAPSHOT.jar`