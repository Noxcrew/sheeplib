import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("fabric-loom")
    kotlin("jvm")
}

// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
internal val Project.libs get() = project.extensions.getByName("libs") as LibrariesForLibs

kotlin {
    explicitApi()
    jvmToolchain(17)
}

tasks {
    named<KotlinCompilationTask<*>>("compileKotlin") {
        compilerOptions {
            freeCompilerArgs.add("-Xjvm-default=all")
        }
    }

    processResources {
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}


repositories {
    mavenCentral()
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

//    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.kotlin)
}
