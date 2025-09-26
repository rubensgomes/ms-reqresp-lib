/*
 * Copyright 2025 Rubens Gomes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("idea")
    id("maven-publish")
    id("version-catalog")
    id("java-library")
    alias(libs.plugins.lombok)
    alias(libs.plugins.release)
    alias(libs.plugins.spotless)
}

// ------------------- Dependencies -------------------
dependencies {
    // Implementation
    implementation(libs.jakarta.annotation.api)
    implementation(libs.jakarta.validation.api)
    implementation(libs.slf4j.api)
    implementation(libs.ms.base.lib)
    testImplementation("org.mockito:mockito-core:5.20.0")

    // Test
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.bundles.logback)
    testImplementation(libs.bundles.junit.jupiter)
    testImplementation(libs.bundles.jakarta.bean.validator)
    testRuntimeOnly(libs.junit.platform.launcher)
}

// ------------------- Idea Plugin -------------------
idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

// ------------------- Java Plugin -------------------
java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to project.properties["title"],
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to project.properties["developerName"],
                "Built-By" to project.properties["developerId"],
                "Build-Jdk" to System.getProperty("java.home"),
                "Created-By" to "${System.getProperty("java.version")} (${System.getProperty("java.vendor")})",
            ),
        )
    }
}

tasks.compileJava {
    // Ensure we have a clean code prior to compilation
    dependsOn("spotlessApply")
}

tasks.javadoc {
    // Exclude Kotlin files from Javadoc since Javadoc can't process them
    exclude("**/*.kt")
    source = sourceSets.main.get().allJava

    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

// ------------------- Maven Publish -------------------
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            versionMapping {
                usage("java-api") { fromResolutionOf("runtimeClasspath") }
                usage("java-runtime") { fromResolutionResult() }
            }

            groupId = project.group.toString()
            artifactId = project.findProperty("artifactId") as String
            version = project.version.toString()

            from(components["java"])

            // POM configuration
            pom {
                name = project.properties["title"] as String
                inceptionYear = "2025"
                packaging = "jar"

                licenses {
                    license {
                        name = project.properties["license"] as String
                        url = project.properties["licenseUrl"] as String
                    }
                }

                developers {
                    developer {
                        id = project.properties["developerId"] as String
                        name = project.properties["developerName"] as String
                        email = project.properties["developerEmail"] as String
                    }
                }

                scm {
                    connection = project.properties["scmConnection"] as String
                    developerConnection = project.properties["scmConnection"] as String
                    url = project.properties["scmUrl"] as String
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri(project.properties["libMavenRepoUrl"] as String)
            credentials {
                username = System.getenv("MAVEN_REPO_USERNAME")
                password = System.getenv("MAVEN_REPO_PASSWORD")
            }
        }
    }
}

// ------------------- Spotless -------------------
val licenseHeaderText =
    """
    /*
     * Copyright 2025 Rubens Gomes
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * You may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    """.trimIndent()

spotless {
    // Java formatting
    java {
        target("src/**/*.java")
        googleJavaFormat()
        removeUnusedImports()
        licenseHeader(licenseHeaderText)
        importOrder("java", "javax", "org", "com", "")
        trimTrailingWhitespace()
        endWithNewline()
    }

    // Kotlin formatting
    kotlin {
        target("src/**/*.kt")
        ktfmt()
        licenseHeader(licenseHeaderText)
        trimTrailingWhitespace()
        endWithNewline()
    }

    // JSON formatting
    json {
        target("src/**/*.json")
        jackson()
    }

    // Kotlin Gradle DSL formatting (root + submodules)
    kotlinGradle {
        target("*.gradle.kts")
        // .editorconfig for fine-grained control
        ktlint().setEditorConfigPath("$rootDir/.editorconfig")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

// ------------------- JVM Test Suite -------------------
tasks.test {
    useJUnitPlatform()
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}

// ------------------- Release Plugin -------------------
release {
    with(git) {
        pushReleaseVersionBranch.set("release")
        pushToRemote.set("origin")
        requireBranch.set("main")
    }
}

// ------------------- Debug Info -----------------------
tasks.register("debugInfo") {
    group = "help"
    description = "Prints debug information for troubleshooting build and publishing issues"

    doLast {
        println("========== DEBUG INFO ==========")

        // Project info
        println("Project: ${project.name}")
        println("Group: ${project.group}")
        println("Version: ${project.version}")
        println("Project dir: ${project.projectDir}")
        println("Build dir:  ${layout.buildDirectory.asFile.get()}")
        println("Gradle version: ${gradle.gradleVersion}")
        println("Kotlin DSL: true")

        // Java info
        println("Java version: ${System.getProperty("java.version")}")
        println("Java vendor: ${System.getProperty("java.vendor")}")
        println("Java home: ${System.getProperty("java.home")}")

        // OS info
        println("OS: ${System.getProperty("os.name")} ${System.getProperty("os.version")} (${System.getProperty("os.arch")})")
        println("User home: ${System.getProperty("user.home")}")

        // Repositories
        println("Repositories:")
        project.repositories.forEach { repo ->
            when (repo) {
                is MavenArtifactRepository -> println(" - ${repo.name}: ${repo.url}")
                is IvyArtifactRepository -> println(" - ${repo.name}: ${repo.url}")
                is FlatDirectoryArtifactRepository -> println(" - ${repo.name}: (flat dir)")
                else -> println(" - ${repo.name}: (unknown type)")
            }
        }

        // Environment variables (print only safe ones)
        val safeEnv = listOf("MAVEN_REPO_USERNAME", "MAVEN_REPO_PASSWORD")
        println("Environment variables (safe subset):")
        safeEnv.forEach { key ->
            println(" - $key = ${System.getenv(key)}")
        }

        // Project properties
        println("Project properties:")
        project.properties.forEach { (k, v) ->
            println(" - $k = $v")
        }

        println("================================")
    }
}
