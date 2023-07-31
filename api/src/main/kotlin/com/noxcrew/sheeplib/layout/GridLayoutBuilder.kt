package com.noxcrew.sheeplib.layout

import com.noxcrew.sheeplib.LayoutConstants
import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.layouts.LayoutSettings
import kotlin.math.max

/**
 * A [LayoutBuilder] backed by a [GridLayout].
 *
 * @see [GridLayout]
 */
public class GridLayoutBuilder(
    x: Int,
    y: Int,
    spacing: Int
) : LayoutBuilder<GridLayout> {

    private val layout = GridLayout(x, y).spacing(spacing)
    public override fun build(): GridLayout = layout

    private var lastRow = -1

    /**
     * Adds an element at a specific row and column.
     *
     * @param row the row to add this element at
     * @param col the column to add this element at
     * @param rowSpan how many rows this element should occupy
     * @param colSpan how many columns this element should occupy
     */
    public fun <T : LayoutElement> T.at(
        row: Int,
        col: Int,
        rowSpan: Int = 1,
        colSpan: Int = 1,
        settings: LayoutSettings = LayoutConstants.CENTRE
    ): T {
        layout.addChild(this, row, col, rowSpan, colSpan, settings)
        lastRow = max(row, lastRow)
        return this
    }

    /**
     * Adds an element on a new row at the bottom of the grid.
     *
     * @param col the column to add this element at
     * @param colSpan how many columns this element should occupy
     */
    public fun <T : LayoutElement> T.atBottom(
        col: Int,
        colSpan: Int = 1,
        settings: LayoutSettings = LayoutConstants.CENTRE
    ): T = this.at(lastRow + 1, col, 1, colSpan, settings)

    /**
     * Starts a new row for use with [onLastRow].
     */
    public fun newRow() {
        lastRow++
    }

    /**
     * Adds an element on the last row of the grid.
     *
     * @param col the column to add this element at
     */
    public fun <T : LayoutElement> T.onLastRow(col: Int, settings: LayoutSettings = LayoutConstants.CENTRE): T =
        this.at(lastRow, col, 1, 1, settings)


    /**
     * Adds an element on a row, filling all currently populated columns to its right.
     *
     * @param row the row to add this element at
     * @param colStart the column at which this element will start
     */
    public fun <T : LayoutElement> T.fillColumn(row: Int, colStart: Int): T =
        this.at(row, colStart, max(1, lastRow - colStart), 1)
}

/**
 * Builds a new grid layout.
 * @param spacing the spacing between elements
 * @param builder the builder
 */
public inline fun GridLayout(spacing: Int, builder: GridLayoutBuilder.() -> Unit): GridLayout =
    GridLayoutBuilder(0, 0, spacing)
        .also(builder)
        .build()

/**
 * Builds a new grid layout, using spacing from this theme.
 * @param builder the builder for the grid
 */
public inline fun Themed.grid(builder: GridLayoutBuilder.() -> Unit): GridLayout =
    GridLayout(theme.dimensions.paddingInner, builder)

