import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    kotlin("multiplatform") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
    `maven-publish`
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_25)
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }

        compilations {
            val main by getting

            compilations {
                tasks.withType<Jar> {
                    manifest {
                        attributes["Main-Class"] = "QuestCommand"
                    }
                    from({
                        configurations["jvmRuntimeClasspath"].filter { it.name.endsWith("jar") }.map { zipTree(it) }
                    })
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                }
            }

            tasks.register<JavaExec>("runQuestCommand") {
                group = "run"
                description = "Runs the JVM entrypoint."
                classpath = main.compileDependencyFiles + main.runtimeDependencyFiles + main.output.allOutputs
                mainClass.set("QuestCommand") // use the fully qualified name if it’s in a package
                standardInput = System.`in`
            }


            val testIntegration by compilations.creating {
                defaultSourceSet {
                    dependencies {
                        implementation(project)
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
                tasks.register<Test>("test-all") {
                    description = "Run unit AND integration tests"
                    dependsOn("jvmTest")
                    dependsOn("test-integration")
                    group = "verification"
                    description = "Runs the integration tests."
                    classpath = compileDependencyFiles + runtimeDependencyFiles + output.allOutputs
                    testClassesDirs = output.classesDirs
                }
            }
            val tools by compilations.creating {
                defaultSourceSet {
                    dependencies {
                        implementation(project)
                        implementation(main.compileDependencyFiles + main.output.classesDirs)
                    }
                }
                tasks.register<JavaExec>("build-data") {
                    group = "build"
                    description = "Apply reflection to do build time code generation."
                    classpath = compileDependencyFiles + runtimeDependencyFiles + output.allOutputs
                    mainClass.set("building.AppBuilder")
                }

                tasks.register<JavaExec>("versionMarker") {
                    group = "build"
                    classpath = compileDependencyFiles + runtimeDependencyFiles + output.allOutputs
//                    workingDir = project.rootDir
                    mainClass.set("building.VersionMarkerKt")
                }
            }
        }
    }
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
            runTask {
                devServerProperty.set(devServerProperty.get().copy(port = 3000))
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
                implementation("org.slf4j:slf4j-nop:2.0.17")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.5")
                implementation("io.ktor:ktor-client-js:2.0.1")
                implementation(npm("localforage", "1.10.0"))
            }
        }
        val jsTest by getting
    }


}

tasks.getByName<Test>("jvmTest") {
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

tasks.named("build") {
    dependsOn("versionMarker")
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
}
