import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import java.net.URL

plugins {
    id("sheeplib.fabric")
    id("sheeplib.publish")
    id("org.jetbrains.dokka") version "1.8.20"
    `maven-publish`
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.8.20")
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.22.0")
    }
}

apply(plugin = "kotlinx-atomicfu")

tasks {
    java {
        archivesName = "sheeplib"
    }
    withType<DokkaTask>().configureEach {
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "© 2023 Noxcrew Online Ltd. LGPL 3.0 licensed"
            separateInheritedMembers = true
            customStyleSheets = listOf("sheeplib-docs.css", "logo-styles.css").map { file("docs/$it") }
        }
        dokkaSourceSets {
            named("main") {
                moduleName.set("SheepLib")
                sourceLink {
                    localDirectory.set(file("src/main/kotlin"))
                    remoteUrl.set(URL("https://github.com/noxcrew/sheeplib/tree/main/api/src/main/kotlin"))
                    remoteLineSuffix.set("#L")
                }
            }
        }
    }
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
}
