package net.bivrik.fancytoasts.toast;

import net.bivrik.fancytoasts.toast.animation.AnimationType;
import net.bivrik.fancytoasts.toast.animation.AdvancementToastAnimation;
import net.bivrik.fancytoasts.toast.texture.TextureType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

import static net.bivrik.fancytoasts.toast.texture.TextureUV.*;

@Environment(EnvType.CLIENT)
public class FancyAdvancementToast {
    private static final int WIDTH = 162;
    private static final int HEIGHT = 70;

    private final AdvancementToastAnimation animation;
    private SoundEvent toastSound;
    private boolean isVisible = true;
    private long time;

    public FancyAdvancementToast(Advancement advancement, AnimationType animationType, TextureType textureType) {
        AdvancementDisplay display = advancement.display().orElse(null);
        if (display != null) {
            animation = AnimationType.ANIMATIONS.get(animationType).get();
            Identifier texture = TextureType.TEXTURES.get(textureType);

            switch (display.getFrame()) {
                case TASK -> {
                    this.animation.setSetup(new RenderSetup(texture, TASK_TEXTURE_UV, display, 0xFFFF00, 0xFFFFFF));
                    toastSound = SoundEvents.ENTITY_ALLAY_ITEM_GIVEN;
                }
                case GOAL -> {
                    this.animation.setSetup(new RenderSetup(texture, GOAL_TEXTURE_UV, display, 0x00FFFF, 0xFFFFFF));
                    toastSound = SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR;
                }
                case CHALLENGE -> {
                    this.animation.setSetup(new RenderSetup(texture, CHALLENGE_TEXTURE_UV, display, 0xEA3CFF, 0x00FFFF));
                    toastSound = SoundEvents.UI_TOAST_CHALLENGE_COMPLETE;
                }
            }
        }
        else {
            throw new IllegalArgumentException("Advancement has no display!");
        }
    }

    public void startSoundQueue(SoundManager manager) {
        manager.play(PositionedSoundInstance.master(SoundEvents.UI_TOAST_IN, 1f, 1.8f));
        manager.play(PositionedSoundInstance.master(toastSound, 1f, 0.8f), animation.getToastSoundTiming() / 50);
        manager.play(PositionedSoundInstance.master(SoundEvents.UI_TOAST_OUT, 1f, 1.8f), animation.getDuration() / 50 - 10);
    }

    public void update(long time) {
        this.time = time;

        if (this.time >= animation.getDuration()) {
            isVisible = false;
        }
    }

    public void draw(DrawContext context, TextRenderer textRenderer) {
        animation.draw(context, textRenderer, this.time, WIDTH, HEIGHT);
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public boolean getVisibility() {
        return isVisible;
    }
}
