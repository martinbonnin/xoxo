plugins {
    id("org.jetbrains.kotlin.jvm").version("1.6.21")
    id("maven-publish")
    id("signing")
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.squareup.okio:okio:3.1.0")
    testImplementation("junit:junit:4.13.2")
}

val emptyJavadocJarTaskProvider = tasks.register("emptyJavadocJar", org.gradle.jvm.tasks.Jar::class.java) {
    archiveClassifier.set("javadoc")
}

val sourcesTaskProvider = tasks.register("javaSourcesJar", Jar::class.java) {
    /**
     * Add a dependency on the compileKotlin task to make sure the generated sources like
     * antlr or SQLDelight get included
     * See also https://youtrack.jetbrains.com/issue/KT-47936
     */
    dependsOn("compileKotlin")

    archiveClassifier.set("sources")
    val sourceSets = project.extensions.getByType(JavaPluginExtension::class.java).sourceSets
    from(sourceSets.getByName("main").allSource)
}

publishing {
    publications {
        create("default", MavenPublication::class.java) {
            from(components.findByName("java"))

            artifact(emptyJavadocJarTaskProvider.get())
            artifact(sourcesTaskProvider.get())

            artifactId = "xoxo"
            groupId = "net.mbonnin.xoxo"
            version = "0.1-SNAPSHOT"

            pom {
                name.set(artifactId)
                description.set(artifactId)

                val githubUrl = "https://github.com/martinbonnin/xoxo"

                url.set(githubUrl)

                scm {
                    url.set(githubUrl)
                    connection.set(githubUrl)
                    developerConnection.set(githubUrl)
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("$githubUrl/main/LICENSE")
                    }
                }

                developers {
                    developer {
                        id.set("Xoxo authors")
                        name.set("Xoxo authors")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            name = "ossSnapshots"
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            credentials {
                username = System.getenv("SONATYPE_NEXUS_USERNAME")
                password = System.getenv("SONATYPE_NEXUS_PASSWORD")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(System.getenv("GPG_PRIVATE_KEY"), System.getenv("GPG_PRIVATE_KEY_PASSWORD"))
    sign(publishing.publications)
}

tasks.withType(Sign::class.java).configureEach {
    isEnabled = !System.getenv("GPG_PRIVATE_KEY").isNullOrBlank()
}

tasks.register("ci") {
    if (isTag()) {
        dependsOn("build")
        dependsOn(tasks.named("publishAllPublicationsToOssStagingRepository"))
    }
}

fun isTag(): Boolean {
    val ref = System.getenv("GITHUB_REF")

    return ref?.startsWith("refs/tags/") == true
}
