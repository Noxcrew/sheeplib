package com.noxcrew.sheeplib

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.Version
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo
import kotlin.jvm.optionals.getOrNull

internal class SheepLibMixinPlugin : IMixinConfigPlugin {

    private val isUsingNoxesium by lazy {
        FabricLoader.getInstance()
            .getModContainer("noxesium")
            .getOrNull()
            ?.let { it.metadata.version > Version.parse("1.1.0") }
            ?: false
    }
    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean = when (mixinClassName) {
        "com.noxcrew.sheeplib.mixin.NoxesiumCompatGuiMixin" -> isUsingNoxesium
        else -> true
    }

    override fun onLoad(mixinPackage: String?): Unit = Unit

    override fun getRefMapperConfig(): String? = null

    override fun acceptTargets(myTargets: MutableSet<String>?, otherTargets: MutableSet<String>?): Unit = Unit

    override fun getMixins(): List<String>? = null

    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ): Unit = Unit

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ): Unit = Unit
}
