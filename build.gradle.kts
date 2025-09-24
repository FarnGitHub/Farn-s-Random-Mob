plugins {
    id("java")
    id("xyz.wagyourtail.unimined") version "1.4.1"
}

group = "test.haha"
version = "2.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
    maven("https://maven.wagyourtail.xyz/releases")
}

val clientA126 by sourceSets.creating
val clientB1102 by sourceSets.creating

tasks.register<Jar>("clientA126Jar") {
    from(clientA126.output)
    archiveClassifier.set("client")
    manifest {
        attributes(
            "JarModAgent-Transforms" to "randomMob.transform",
            "JarModAgent-Refmaps" to "randomMob-refmap.json"
        )
    }
}

tasks.register<Jar>("clientB1102Jar") {
    from(clientB1102.output)
    archiveClassifier.set("client")
    manifest {
        attributes(
            "JarModAgent-Transforms" to "randomMob.transform",
            "JarModAgent-Refmaps" to "randomMob-refmap.json"
        )
    }
}

unimined.minecraft(clientA126) {
    version ("a1.2.6")
    side("client") // a trick because we named them based on the sides

    mappings {
        retroMCP("a1.2.6")
    }

    jarMod {
        transforms("randomMob.transform")
    }

}

unimined.minecraft(clientB1102) {
    version ("b1.1_02")
    side("client") // a trick because we named them based on the sides

    mappings {
        retroMCP("b1.1")
    }

    jarMod {
        transforms("randomMob.transform")
    }

}

dependencies {
}

tasks.withType<JavaCompile> {
    if (JavaVersion.current().isJava9Compatible) {
        options.release.set(8)
    }
}

tasks.jar {
    enabled = false
}