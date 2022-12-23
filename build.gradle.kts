@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform") version "1.7.21"
    kotlin("plugin.serialization") version "1.7.21"
    `maven-publish`
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }

        sourceSets {
            val jvmTestIntegration by creating {
                dependsOn(sourceSets["jvmMain"])
            }
            val jvmTools by creating {
                dependsOn(sourceSets["jvmMain"])
            }
        }
        compilations {
            val main by getting
            val testIntegration by compilations.creating {
                defaultSourceSet {
                    dependencies {
                        implementation(main.compileDependencyFiles + main.output.classesDirs)
                        implementation(kotlin("test-junit"))
                    }
                }
                tasks.register<Test>("test-integration") {
                    group = "verification"
                    description = "Runs the integration tests."
                    classpath = compileDependencyFiles + runtimeDependencyFiles + output.allOutputs
                    testClassesDirs = output.classesDirs
                }
            }
            val tools by compilations.creating {
                defaultSourceSet {
                    dependencies {
                        implementation(main.compileDependencyFiles + main.output.classesDirs)
                    }
                }
                tasks.register<JavaExec>("build-data") {
                    group = "build"
                    description = "Apply reflection to do build time code generation."
                    classpath = compileDependencyFiles + runtimeDependencyFiles + output.allOutputs
                    setMain("building.AppBuilder")
                }
            }
        }
        withJava()
    }
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            runTask {
                devServer = devServer?.copy(port = 3000)
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
                implementation("io.ktor:ktor-client-content-negotiation:2.0.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.reflections:reflections:0.10.2")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
                implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
                implementation("io.ktor:ktor-client-cio:2.0.1")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.5")
                implementation("io.ktor:ktor-client-js:2.0.1")
            }
        }
        val jsTest by getting
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
//    publications {
//        register<MavenPublication>("gpr") {
//        }
//    }
}
