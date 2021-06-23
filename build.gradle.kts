plugins {
    kotlin("jvm") version "1.5.10" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
}

group = "ru.otus.otuskotlin.blogas"
version = "0.0.1"

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
    }
}
