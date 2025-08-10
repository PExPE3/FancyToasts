package net.bivrik.fancytoasts.toast.animation;

import net.bivrik.fancytoasts.toast.RenderSetup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;

@Environment(EnvType.CLIENT)
public abstract class AdvancementToastAnimation {
    private RenderSetup setup;

    public void setSetup(RenderSetup setup) {
        this.setup = setup;
    }

    public RenderSetup getSetup() {
        return setup;
    }

    public void drawIcon(DrawContext context) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, getSetup().texture(), 68, 0, getSetup().uv()[1].getU(), getSetup().uv()[1].getV(), 26, 26, 256, 256);
        context.drawItemWithoutEntity(getSetup().display().getIcon(), 73, 5);
    }
    public void drawBanner(DrawContext context) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, getSetup().texture(), 0, 5, getSetup().uv()[0].getU(), getSetup().uv()[0].getV(), 162, 14, 256, 256);
    }
    public void drawBackground(DrawContext context) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, getSetup().texture(), 0, 20, 0, 0, 162, 40, 256, 256);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, getSetup().texture(), 144, 56, 0, 108, 9, 14, 256, 256);
    }

    public record Appearance(int duration, int startPoint) {}

    public float getProgress(long time, int duration, int startPoint) {
        return Math.min(1.0f, Math.max(0.0f, (float) (time - startPoint) / duration));
    }
    public float getProgress(long time, Appearance appearance) {
        return getProgress(time, appearance.duration(), appearance.startPoint());
    }

    public abstract void draw(DrawContext context, TextRenderer textRenderer, long time, int toastWidth, int toastHeight);

    public abstract int getDuration();

    public abstract int getToastSoundTiming();
}
