package com.noxcrew.sheeplib.util

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation

/**
 * An icon for use as a GUI texture.
 * @param location the resource location of the texture file
 * @param hoverLocation the resource location of an alternative "hovered" texture file,
 * with the same dimensions as [location]
 * @param height the texture's native height in pixels
 * @param width the texture's native width in pixels
 */
public data class Icon(
    val location: ResourceLocation,
    val height: Int,
    val width: Int,
    val hoverLocation: ResourceLocation = location,
) {

    /**
     * Blits the texture onto the screen.
     *
     * @param guiGraphics a [GuiGraphics] instance to render with
     * @param x the X coordinate to blit to (left edge)
     * @param y the Y coordinate to blit to (top edge)
     * @param scale a factor to scale the icon up by. Defaults to 1.
     */
    public fun blit(guiGraphics: GuiGraphics, x: Int, y: Int, scale: Int = 1, isHovered: Boolean = false) {
        guiGraphics.blit(
            RenderType::guiTextured,
            if (isHovered) hoverLocation else location,
            x,
            y,
            0f,
            0f,
            width * scale,
            height * scale,
            width * scale,
            height * scale,
        )
    }
}
