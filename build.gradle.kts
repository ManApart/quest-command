plugins {
    kotlin("multiplatform") version "1.6.21"
    `maven-publish`
    kotlin("plugin.serialization") version "1.6.21"
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.reflections:reflections:0.10.2")
                implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
                implementation("io.ktor:ktor-client-core:2.0.1")
                implementation("io.ktor:ktor-client-cio:2.0.1")
                implementation("io.ktor:ktor-client-content-negotiation:2.0.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.1")
            }
        }
        val jvmTest by getting
        val jvmTestIntegration by creating {
            dependsOn(jvmMain)
            dependsOn(jvmTest)
            jvm()
        }
        val jvmTools by creating {
            dependsOn(jvmMain)
            jvm()
            //TODO - can't run main functions in this source set

            //    compileClasspath += main.output + main.compileClasspath
            //    runtimeClasspath += output + compileClasspath
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.5")
            }
        }
        val jsTest by getting
    }

    task("buildData", type = JavaExec::class) {
        group = "build"
        main = "building.AppBuilder"
        classpath = sourceSets["jvmTools"].kotlin
    }

    task("test-integration", type = Test::class) {
        val integration = sourceSets["jvmTestIntegration"]
        group = "verification"
        description = "Runs the integration tests."
//        testClassesDirs = integration.kotlin.destinationDirectory
        //testClassesDirs = integration.output.classesDirs
        classpath = integration.kotlin
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
}

tasks.getByName<Test>("test") {
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
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
