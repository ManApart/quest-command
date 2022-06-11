# Quest Command

[![Build and Test](https://github.com/ManApart/QuestCommand/actions/workflows/runTests.yml/badge.svg)](https://github.com/ManApart/QuestCommand/actions/workflows/runTests.yml)

An open world rpg with intense levels of interaction, experienced through the command prompt.

See the [wiki](https://github.com/ManApart/QuestCommand/wiki) for more information

## Building / Running

### Building

Run `gradlew buildData`. This creates and updates some files via reflection so that they don't need to be done at runtime. This only needs to be re-run when you change certain files (like adding new commands, events, event listeners, etc)

Run `gradlew build` to build a jar. This should be found in `QuestCommand/build/libs`

Run `gradlew publishToMavenLocal` to push to local maven for other projects to consume. `implementation("org.rak.manapart:quest-command:dev")`

#### Gradle failing to build
`./gradlew: 68: Syntax error: word unexpected (expecting "in")` is due to [differences between windows and linux](https://stackoverflow.com/questions/55342922/gradle-gradlew-expecting-in) and can be solved by `sed -i.bak 's/\r$//' gradlew`

#### PackageName Unresolved
Getting `Unresolved reference: packageName`? You're probably running gradle on something less than 11. Check `java -version` on the terminal and Settings > Build Execution Deployment > Gradle has Gradle JVM set appropriately

### Running
Try any one of the below: 

A) Grab a release from [github](https://github.com/ManApart/QuestCommand/releases) and run `java -jar ./quest-command-jvm-dev.jar`

B) Grab the image from [docker hub](https://hub.docker.com/repository/docker/manapart/quest-command) and run `docker run -it manapart/quest-command:stable`

C) Clone the project and build it.
```
./gradlew build
java -jar ./build/libs/quest-command-jvm-dev.jar
```

#### Running in the browser

`gradlew jsBrowserDevelopmentRun`


### Pushing to web

```
./gradlew jsBrowserDistribution 
aws s3 sync build/distributions/ s3://austinkucera.com/games/quest-command/
```

### Visual Git Log

`gource -a 1 -s 1 --file-idle-time 0 --key -f`
