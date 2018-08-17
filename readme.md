# Quest Command

An open world rpg with intense levels of interact, experienced through the command prompt

## Building / Running

### Building

Run `mvn clean install` and then `mvn package -Dmaven.test.skip=true` to build a jar. This should be found in `QuestCommand/target` (`quest-command-1.0-SNAPSHOT-jar-with-dependencies` or `quest-command-1.0-SNAPSHOT`)

TODO - tests run correctly in intellij but not through maven

### Running
Navigate to `QuestCommand/target` and run `java -jar quest-command-1.0-SNAPSHOT-jar-with-dependencies.jar`