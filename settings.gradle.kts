rootProject.name = "blog"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val ktLintVersion: String by settings
        val openApiVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        id("org.jlleitschuh.gradle.ktlint") version ktLintVersion
        kotlin("plugin.serialization") version kotlinVersion

        id("org.openapi.generator") version openApiVersion
    }
}

include("ok-m1l1")
include("ok-blog-mp-common")
include("ok-m2l2-testing")
include("ok-blog-be-transport-openapi")
include("ok-blog-be-common")
include("ok-blog-be-transport-mapping-openapi")
