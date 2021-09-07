plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    // transport models
    implementation(project(":ok-blog-be-common"))

    testImplementation(kotlin("test-junit"))
}
