plugins {
    kotlin("jvm") apply false
    id("org.jlleitschuh.gradle.ktlint")
}

group = "ru.otus.otuskotlin.blog"
version = "0.0.1"
description = "OTUS Kotlin course project"

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        maven("https://plugins.gradle.org/m2/")
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}
