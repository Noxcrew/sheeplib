

plugins {
    id("sheeplib.fabric")
    id("sheeplib.publish")
    `maven-publish`
}

dependencies {
    compileOnlyApi(libs.kotlin.coroutines) // provided by fabric language kotlin
    api(project(":api", configuration = "namedElements"))
}

tasks {
    jar {
        archiveBaseName = "sheeplib-coroutines"
    }
}
