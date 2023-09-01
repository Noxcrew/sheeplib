import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    `maven-publish`
    `java-library`
}

tasks {
    java {
        withSourcesJar()
    }
}

// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
internal val Project.libs get() = project.extensions.getByName("libs") as LibrariesForLibs

dependencies {
    compileOnly(libs.jb.annotations)
}

publishing {
    repositories {
        maven {
            name = "noxcrew-public"
            url = uri("https://maven.noxcrew.com/public")
            credentials {
                username = System.getenv("NOXCREW_MAVEN_PUBLIC_USERNAME")
                password = System.getenv("NOXCREW_MAVEN_PUBLIC_PASSWORD")
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
