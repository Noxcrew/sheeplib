import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

plugins {
    id("sheeplib.fabric")
    id("org.jetbrains.dokka") version "1.8.20"
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.8.20")
    }
}

tasks.withType<DokkaTask>().configureEach {
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
