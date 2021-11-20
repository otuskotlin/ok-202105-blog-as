val ktorVersion: String by project

fun DependencyHandler.ktor(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-$module:$version"

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("application")
    id("com.bmuschko.docker-java-application")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

docker {
    javaApplication {
        mainClassName.set(application.mainClass.get())
        baseImage.set("adoptopenjdk/openjdk11:alpine-jre")
        maintainer.set("alexsumin")
        ports.set(listOf(8080))
        val imageName = project.name
        images.set(
            listOf(
                "$imageName:${project.version}",
                "$imageName:latest"
            )
        )
        jvmArgs.set(listOf("-Xms256m", "-Xmx512m"))
    }
}

dependencies {
    val logbackVersion: String by project
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(ktor("server-core")) // "io.ktor:ktor-server-core:$ktorVersion"
    implementation(ktor("server-netty")) // "io.ktor:ktor-ktor-server-netty:$ktorVersion"
    implementation(ktor("jackson")) // "io.ktor:ktor-ktor-jackson:$ktorVersion"

    // logging if you want
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // transport models
    implementation(project(":ok-blog-be-common"))
    implementation(project(":ok-blog-be-transport-openapi"))
    implementation(project(":ok-blog-be-transport-mapping-openapi"))
    // service
    implementation(project(":ok-blog-be-post-service-openapi"))
    // logic
    implementation(project(":ok-blog-be-logics"))
    // bd
    implementation(project(":ok-blog-be-post-repo-inmemory"))
    testImplementation(project(":ok-blog-be-stubs"))
    testImplementation(kotlin("test-junit"))
    testImplementation(ktor("server-test-host")) // "io.ktor:ktor-server-test-host:$ktorVersion"
    testImplementation(project(":ok-blog-be-stubs"))
}
