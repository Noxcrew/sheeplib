import org.gradle.accessors.dm.LibrariesForLibs

val VERSION = "1.2.0-SNAPSHOT"

allprojects {
    group = "com.noxcrew.sheeplib"
    afterEvaluate {
        version = "$VERSION+${the<LibrariesForLibs>().versions.minecraft.get()}"
    }
}
