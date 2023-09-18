package com.noxcrew.sheeplib.test

import com.noxcrew.sheeplib.util.lighten
import com.noxcrew.sheeplib.util.opacity
import com.noxcrew.sheeplib.util.opaqueColor
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class IntColorUtilTests {

    @Test
    fun testOpaqueColor() {
        assertEquals(0xff00ff00u.toInt(), 0x00ff00.opaqueColor())
        assertEquals(0xffffffffu.toInt(), 0xffffff.opaqueColor())
    }

    @Test
    fun testOpacity() {
        assertEquals(0xff_ff_ff_ffu, (0xff_ff_ff opacity 0xff).toUInt())
        assertEquals(0x70_ff_00_ff, 0xff00ff opacity 0x70)
    }

    @Test
    fun testLighten() {
        assertEquals(0x707070, 0x707070 lighten 0f)
        assertEquals(0xff66ff, 0xff33ff lighten 1f)
        assertEquals(0x999999, 0x666666 lighten 0.5f)
    }
}
