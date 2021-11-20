plugins {
    kotlin("jvm")
}

dependencies {

    val ehcacheVersion: String by project
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":ok-blog-be-common"))

    implementation("org.ehcache:ehcache:$ehcacheVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation(project(":ok-blog-be-post-repo-test"))
    testImplementation(project(":ok-blog-be-stubs"))
}
