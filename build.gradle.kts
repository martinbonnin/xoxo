plugins {
    id("org.jetbrains.kotlin.jvm").version("1.6.21")
    id("net.mbonnin.sjmp").version("0.2")
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.squareup.okio:okio:3.1.0")
    testImplementation("junit:junit:4.13.2")
}

tasks.withType(JavaCompile::class.java) {
    options.release.set(8)
}

sjmp {
    jvmProject {
        publication {
            groupId = "net.mbonnin.xoxo"
            version = "0.2-SNAPSHOT"
            simplePom {
                githubRepository = "martinbonnin/xoxo"
                githubLicensePath = "LICENSE"
                license = "MIT License"
            }
        }
    }
}