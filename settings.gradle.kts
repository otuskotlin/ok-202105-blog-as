rootProject.name = "blog"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val ktLintVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        id("org.jlleitschuh.gradle.ktlint") version ktLintVersion
    }
}

include("ok-m1l1")
include("ok-blog-mp-common")
include("ok-m2l2-testing")
