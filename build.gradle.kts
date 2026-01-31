import org.gradle.accessors.dm.LibrariesForLibs

val VERSION = "1.5.1"

val isBuildingSnapshot = System.getenv("IS_BUILDING_SNAPSHOT") == "true"

allprojects {
    group = "com.noxcrew.sheeplib"
    afterEvaluate {
        version = "$VERSION+${the<LibrariesForLibs>().versions.minecraft.get()}${if (isBuildingSnapshot) "-SNAPSHOT" else ""}"
    }
}
