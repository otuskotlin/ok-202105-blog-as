plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":ok-blog-be-common"))
    implementation(project(":ok-blog-be-transport-openapi"))

    testImplementation(kotlin("test"))
}
