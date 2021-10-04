plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":ok-blog-be-common"))
    implementation(project(":ok-blog-be-transport-openapi"))
    implementation(project(":ok-blog-be-transport-mapping-openapi"))
    implementation(project(":ok-blog-be-stubs"))
}
