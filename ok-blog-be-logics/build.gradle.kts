plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":ok-blog-be-common"))
    implementation(project(":ok-blog-be-common-cor"))
    implementation(project(":ok-blog-be-stubs"))
    implementation(project(":ok-blog-be-common-validation"))

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    testImplementation(project(":ok-blog-be-post-repo-inmemory"))
}
