package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.theme.DefaultTheme
import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.gui.layouts.Layout

/**
 * A dialog that throws an exception when built.
 * This is a class and not a simple dialog to build lazily.
 */
public class ExceptionDialog(x: Int, y: Int): Dialog(x, y), Themed by DefaultTheme {
    override fun layout(): Layout {
        error("Test exception from the exception dialog. This is not a bug.")
    }
}
