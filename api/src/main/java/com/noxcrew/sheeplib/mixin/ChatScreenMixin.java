package com.noxcrew.sheeplib.mixin;

import com.noxcrew.sheeplib.DialogContainer;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Adaptations to allow {@link ChatScreen} to handle multiple children.
 */
@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen implements GuiEventListener {

    @Shadow
    protected EditBox input;

    protected ChatScreenMixin(Component component) {
        super(component);
    }

    /**
     * Adds the dialog container screen as a child of this screen.
     */
    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    public void init(String string, CallbackInfo ci) {
//        this.addWidget(DialogContainerScreen.INSTANCE);
        this.addWidget(DialogContainer.INSTANCE);
    }

    /**
     * Delegate mouse scroll to the focused child, and then all other children, before handling.
     */
    @Inject(
            method = "mouseScrolled",
            at = @At("HEAD"),
            cancellable = true
    )
    public void mouseScrolled(double d, double e, double f, CallbackInfoReturnable<Boolean> cir) {
        final var focused = getFocused();
        if (focused != null && focused.mouseScrolled(d, e, f)) {
            cir.setReturnValue(true);
            return;
        }

        for (final var child : children()) {
            if (child == focused) continue;
            if (child.mouseScrolled(d, e, f)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }

    /**
     * Delegate mouse click to the focused child, and then all other children, before handling.
     */
    @Inject(
            method = "mouseClicked",
            at = @At("HEAD"),
            cancellable = true
    )
    public void mouseClicked(double d, double e, int i, CallbackInfoReturnable<Boolean> cir) {
        for (final var child : children()) {
            if (child != null && child.mouseClicked(d, e, i)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }


    /**
     * Delegate mouse release to all children.
     */
    @Override
    public boolean mouseReleased(double d, double e, int i) {
        setDragging(false);
        for (final var child : children()) {
            if (child != null && child.mouseReleased(d, e, i)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        return DialogContainer.INSTANCE.mouseDragged(d, e, i, f, g);
    }

    /**
     * Attempt to pass char type events to {@link DialogContainer} first.
     * This is necessary as {@link ChatScreen} assumes that the input element is the only child.
     */
    @Override
    public boolean charTyped(char c, int i) {
        return DialogContainer.INSTANCE.charTyped(c, i) || input.charTyped(c, i);
    }

    /**
     * Pass key press events to {@link DialogContainer} first.
     */
    @Inject(
            method = "keyPressed",
            at = @At("HEAD"),
            cancellable = true
    )
    public void keyPressed(int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        if (DialogContainer.INSTANCE.keyPressed(i, j, k)) {
            cir.setReturnValue(true);
        }
    }

    /**
     * Clear the dialog container's focus when the screen is closed.
     */
    @Inject(
            method = "removed",
            at = @At("TAIL")
    )
    public void removed(CallbackInfo ci) {
        DialogContainer.INSTANCE.setFocused(null);
    }
}
