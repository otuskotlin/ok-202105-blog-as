rootProject.name = "blog"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val ktLintVersion: String by settings
        val openApiVersion: String by settings
        val bmuschkoVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        id("org.jlleitschuh.gradle.ktlint") version ktLintVersion
        kotlin("plugin.serialization") version kotlinVersion

        id("org.openapi.generator") version openApiVersion
        id("com.bmuschko.docker-java-application") version bmuschkoVersion
    }
}

include("ok-m1l1")
include("ok-blog-mp-common")
include("ok-m2l2-testing")
include("ok-blog-be-transport-openapi")
include("ok-blog-be-common")
include("ok-blog-be-transport-mapping-openapi")
include("ok-blog-be-stubs")
include("ok-blog-be-post-service-openapi")
include("ok-blog-be-post-app-ktor")
include("ok-blog-be-common-cor")
include("ok-blog-be-logics")
include("ok-blog-be-common-validation")
include("ok-blog-be-post-app-rabbit")
include("ok-blog-be-post-repo-test")
include("ok-blog-be-post-repo-inmemory")
include("ok-blog-be-post-repo-sql")
