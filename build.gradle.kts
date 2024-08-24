import org.gradle.accessors.dm.LibrariesForLibs

val VERSION = "1.3.5"

allprojects {
    group = "com.noxcrew.sheeplib"
    afterEvaluate {
        version = "$VERSION+${the<LibrariesForLibs>().versions.minecraft.get()}"
    }
}
