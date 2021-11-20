plugins {
    kotlin("jvm")
}

tasks {
    withType<Test> {
        environment("ok.blog.sql_drop_db", true)
        environment("ok.blog.sql_fast_migration", true)
    }
}

dependencies {
    val exposedVersion: String by project
    val postgresDriverVersion: String by project
    val testContainersVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":ok-blog-be-common"))

    implementation("org.postgresql:postgresql:$postgresDriverVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    testImplementation(project(":ok-blog-be-post-repo-test"))
}
