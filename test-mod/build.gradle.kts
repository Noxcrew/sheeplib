plugins {
    id("sheeplib.fabric")
}

loom.runConfigs["client"].ideConfigGenerated(true)

dependencies {
    implementation(project(":api", configuration = "namedElements"))
}
