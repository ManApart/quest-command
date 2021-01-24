import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20"
}

group = "org.rak.manapart"
version = "dev"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.reflections:reflections:0.9.10")
    implementation("org.jline:jline:3.8.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
    testImplementation("junit:junit:4.12")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.4.20")

//    testImplementation(kotlin("test-testng"))
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

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

task("integrationTest", type = Test::class) {
    val integration = sourceSets["integrationTest"]
    description = "Runs the integration tests."
    group = "verification"
    testClassesDirs = integration.output.classesDirs
    classpath = integration.runtimeClasspath

    outputs.upToDateWhen { false }
    mustRunAfter(tasks["test"])
}

task("buildData", type = JavaExec::class) {
    main = "building.AppBuilder"
    classpath = sourceSets["tools"].runtimeClasspath
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    languageVersion = "1.4"
}