package com.noxcrew.sheeplib.mixin;

import com.noxcrew.sheeplib.DialogContainer;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
            method = "init",
            at = @At("TAIL")
    )
    public void init(CallbackInfo ci) {
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
    public void mouseScrolled(double d, double e, double f, double g, CallbackInfoReturnable<Boolean> cir) {
        final var focused = getFocused();
        if (focused != null && focused.mouseScrolled(d, e, f, g)) {
            cir.setReturnValue(true);
            return;
        }

        for (final var child : children()) {
            if (child == focused) continue;
            if (child.mouseScrolled(d, e, f, g)) {
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
    public void mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        for (final var child : children()) {
            if (child != null && child.mouseClicked(mouseButtonEvent, bl)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }


    /**
     * Delegate mouse release to all children.
     */
    @Override
    public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
        setDragging(false);
        for (final var child : children()) {
            if (child != null && child.mouseReleased(mouseButtonEvent)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double d, double e) {
        return DialogContainer.INSTANCE.mouseDragged(mouseButtonEvent, d, e);
    }

    /**
     * Attempt to pass char type events to {@link DialogContainer} first.
     * This is necessary as {@link ChatScreen} assumes that the input element is the only child.
     */
    @Override
    public boolean charTyped(CharacterEvent characterEvent) {
        return DialogContainer.INSTANCE.charTyped(characterEvent) || input.charTyped(characterEvent);
    }

    /**
     * Pass key press events to {@link DialogContainer} first.
     */
    @Inject(
            method = "keyPressed",
            at = @At("HEAD"),
            cancellable = true
    )
    public void keyPressed(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
        if (DialogContainer.INSTANCE.keyPressed(keyEvent)) {
            cir.setReturnValue(true);
        }
    }

    @Redirect(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;keyPressed(Lnet/minecraft/client/input/KeyEvent;)Z"
            )
    )
    public boolean redirectSuper(Screen instance, KeyEvent keyEvent) {
        return keyEvent.key() != GLFW.GLFW_KEY_UP &&
                keyEvent.key() != GLFW.GLFW_KEY_DOWN &&
                super.keyPressed(keyEvent);
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
