import org.gradle.accessors.dm.LibrariesForLibs

val VERSION = "1.4.1"

allprojects {
    group = "com.noxcrew.sheeplib"
    afterEvaluate {
        version = "$VERSION+${the<LibrariesForLibs>().versions.minecraft.get()}"
    }
}
