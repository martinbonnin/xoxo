import com.gradleup.librarian.gradle.PomMetadata
import com.gradleup.librarian.gradle.Signing
import com.gradleup.librarian.gradle.configureJavaCompatibility
import com.gradleup.librarian.gradle.configureKotlinCompatibility
import com.gradleup.librarian.gradle.configurePublishing

plugins {
    id("org.jetbrains.kotlin.jvm").version("2.1.0")
    id("com.gradleup.librarian").version("0.0.7")
    id("com.gradleup.nmcp").version("0.0.8")
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.squareup.okio:okio:3.4.0")
    testImplementation("junit:junit:4.13.2")
}

configureJavaCompatibility(8)
configureKotlinCompatibility("1.6.21")

configurePublishing(
    true,
    true,
    PomMetadata(
        "net.mbonnin.xoxo",
        "xoxo",
        "0.4.1-SNAPSHOT",
        "xoxo",
        "https://github.com/martinbonnin/xoxo",
        "xoxo authors",
        "MIT"
    ),
    Signing(
        privateKey = System.getenv("GPG_PRIVATE_KEY"),
        privateKeyPassword = System.getenv("GPG_PRIVATE_KEY_PASSWORD")
    )
)

nmcp {
    publish("default") {
        username = System.getenv("OSSRH_USER")
        password = System.getenv("OSSRH_PASSWORD")
        // publish manually from the portal
        publicationType = "USER_MANAGED"
    }
}