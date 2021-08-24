import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.20"
}

group = "org.rak.manapart"
version = "dev"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.reflections:reflections:0.9.12")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.0")
    //Upgrading jackson breaks secondary constructor objects (see MultiObjectParserTest)
//    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.21")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.21")
}

sourceSets.getByName("main") {
    java.srcDir("src/main/kotlin")
    resources.srcDir("src/main/resource")
}

sourceSets.getByName("test") {
    java.srcDir("src/test/kotlin")
    resources.srcDir("src/test/resource")
}

sourceSets.create("tools") {
    val main = sourceSets["main"]
    java.srcDir("src/tools/kotlin")

    compileClasspath += main.output + main.compileClasspath
    runtimeClasspath += output + compileClasspath
}

sourceSets.create("integrationTest") {
    val main = sourceSets["main"]
    val tools = sourceSets["tools"]

    java.srcDir("src/test-integration/kotlin")
    resources.srcDir("src/test-integration/resource")

    compileClasspath += main.output + tools.output + main.compileClasspath + tools.compileClasspath + configurations["testRuntimeClasspath"]
    runtimeClasspath += output + compileClasspath + sourceSets["test"].runtimeClasspath
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    languageVersion = "1.5"
    jvmTarget = "11"
}

task("buildData", type = JavaExec::class) {
    main = "building.AppBuilder"
    classpath = sourceSets["tools"].runtimeClasspath
}

task("test-integration", type = Test::class) {
    val integration = sourceSets["integrationTest"]
    description = "Runs the integration tests."
    group = "verification"
    testClassesDirs = integration.output.classesDirs
    classpath = integration.runtimeClasspath

    outputs.upToDateWhen { false }
    mustRunAfter(tasks["test"])
}

task("test-all") {
    description = "Run unit AND integration tests"
    dependsOn("test")
    dependsOn("test-integration")
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

}
