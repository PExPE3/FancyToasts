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
    private SoundManager soundManager;
    private int playedSoundsCount = 0;

    public FancyAdvancementToast(Advancement advancement, AnimationType animationType, TextureType textureType) {
        AdvancementDisplay display = advancement.display().orElse(null);
        if (display != null) {
            animation = AnimationType.ANIMATIONS.get(animationType).get();
            Identifier texture = TextureType.TEXTURES.get(textureType);

            switch (display.getFrame()) {
                case AdvancementFrame.TASK -> {
                    this.animation.setSetup(new RenderSetup(texture, TASK_TEXTURE_UV, display, Colors.YELLOW, Colors.WHITE));
                    toastSound = SoundEvents.ENTITY_ALLAY_ITEM_GIVEN;
                }
                case AdvancementFrame.GOAL -> {
                    this.animation.setSetup(new RenderSetup(texture, GOAL_TEXTURE_UV, display, Colors.CYAN, Colors.WHITE));
                    toastSound = SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR;
                }
                case AdvancementFrame.CHALLENGE -> {
                    this.animation.setSetup(new RenderSetup(texture, CHALLENGE_TEXTURE_UV, display, 0xFFEA3CFF, Colors.CYAN));
                    toastSound = SoundEvents.UI_TOAST_CHALLENGE_COMPLETE;
                }
            }
        }
        else {
            throw new IllegalArgumentException("Advancement has no display!");
        }
    }

    public void startSounds(SoundManager manager) {
        soundManager = manager;
        manager.play(PositionedSoundInstance.master(SoundEvents.UI_TOAST_IN, 1f, 1.8f));
    }

    public void update(long time) {
        this.time = time;

        if (this.time >= animation.getDuration()) {
            isVisible = false;
        }

        int timeInSeconds = (int) (this.time / 50);
        if (playedSoundsCount == 0 && timeInSeconds == animation.getToastSoundTiming() / 50) {
            soundManager.play(PositionedSoundInstance.master(toastSound, 1f, 0.75f));
            playedSoundsCount++;
        }
        if (playedSoundsCount == 1 && timeInSeconds == animation.getDuration() / 50 - 10) {
            soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_TOAST_OUT, 1f, 1.7f));
            playedSoundsCount++;
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
