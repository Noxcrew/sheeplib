plugins {
    id("sheeplib.fabric")
}

loom.runConfigs["client"].ideConfigGenerated(true)

dependencies {
    modImplementation(libs.fabric.api)
    implementation(project(":api", configuration = "namedElements"))
    implementation(project(":coroutines", configuration = "namedElements"))
}
