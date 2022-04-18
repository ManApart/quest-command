import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    `maven-publish`
    kotlin("plugin.serialization") version "1.6.10"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.10")
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

tasks.getByName<Test>("test"){
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}


task("test-integration", type = Test::class) {
    val integration = sourceSets["integrationTest"]
    description = "Runs the integration tests."
    group = "verification"
    testClassesDirs = integration.output.classesDirs
    classpath = integration.runtimeClasspath
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }

    outputs.upToDateWhen { false }
    mustRunAfter(tasks["test"])
}

task("test-all") {
    description = "Run unit AND integration tests"
    dependsOn("test")
    dependsOn("test-integration")
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "QuestCommandKt"
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ManApart/quest-command")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }

        create<MavenPublication>("maven") {
            groupId = "org.rak.manapart"
            artifactId = "quest-command"
            version = "SNAPSHOT"
            from(components["java"])
        }
    }
}
