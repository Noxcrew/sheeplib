import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("fabric-loom")
    kotlin("jvm")
}

// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
internal val Project.libs get() = project.extensions.getByName("libs") as LibrariesForLibs

repositories {
    mavenCentral()
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.kotlin)
}
