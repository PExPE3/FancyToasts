package net.bivrik.fancytoasts.toast.animation.preset;

import net.bivrik.fancytoasts.toast.animation.AdvancementToastAnimation;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.ColorHelper;

import java.util.List;

import static net.bivrik.fancytoasts.utilites.EasingHelper.easeInLerp;
import static net.bivrik.fancytoasts.utilites.EasingHelper.easeOutLerp;
import static net.bivrik.fancytoasts.utilites.TextRendererHelper.drawCenteredText;

public class StandardAnimation extends AdvancementToastAnimation {
    private static final Appearance ICON_APPEARANCE = new Appearance(2000, 0);
    private static final Appearance BANNER_APPEARANCE = new Appearance(500, 1500);
    private static final Appearance BACKGROUND_APPEARANCE = new Appearance(800, 1600);
    private static final Appearance TEXT_APPEARANCE = new Appearance(1000, 2000);

    private static final int FADE_OUT_DURATION = 2000;
    private static final int DURATION = 6000 + FADE_OUT_DURATION;

    @Override
    public void draw(DrawContext context, TextRenderer textRenderer, long time, int toastWidth, int toastHeight) {
        float iconAppearProgress = getProgress(time, ICON_APPEARANCE);
        float bannerAppearProgress = getProgress(time, BANNER_APPEARANCE);
        float backgroundAppearProgress = getProgress(time, BACKGROUND_APPEARANCE);
        float textAppearProgress = getProgress(time, TEXT_APPEARANCE);
        float fadeOutProgress = getProgress(time, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

        var matrix = context.getMatrices();

        if (fadeOutProgress > 0) {
            float fadeOutY = easeInLerp(0, -80, fadeOutProgress);

            matrix.pushMatrix();
            matrix.translate(0, fadeOutY);
        }

        if (backgroundAppearProgress > 0) {
            matrix.pushMatrix();
            if (backgroundAppearProgress != 1) {
                int y = easeOutLerp(-200, 0, backgroundAppearProgress);

                matrix.translate(0, y);
            }
            drawBackground(context);
            matrix.popMatrix();
        }

        if (bannerAppearProgress > 0) {
            matrix.pushMatrix();
            if (bannerAppearProgress != 1) {
                float xScale = easeOutLerp(0.0f, 1.0f, bannerAppearProgress);

                matrix.translate(81, 0);
                matrix.scale(xScale, 1.0f);
                matrix.translate(-81, 0);
            }
            drawBanner(context);
            matrix.popMatrix();
        }

        if (iconAppearProgress > 0) {
            matrix.pushMatrix();
            if (iconAppearProgress != 1) {
                int y = easeOutLerp(-100, 0, iconAppearProgress);
                float scale = easeOutLerp(0.0f, 1.0f, iconAppearProgress);

                matrix.translate(81, 13);
                matrix.scale(scale, scale);
                matrix.translate(-81, -13);
                matrix.translate(0, y);
            }
            matrix.translate(0, (float) Math.sin(time / 500.0) * 2);
            drawIcon(context);
            matrix.popMatrix();
        }

        if (textAppearProgress > 0) {
            int toastColor = ColorHelper.withAlpha(textAppearProgress, getSetup().toastColor());
            int titleColor = ColorHelper.withAlpha(textAppearProgress, getSetup().titleColor());

            drawCenteredText(context, textRenderer, getSetup().display().getFrame().getToastText().asOrderedText(), toastWidth / 2, 25, toastColor);
            List<OrderedText> list = textRenderer.wrapLines(getSetup().display().getTitle(), toastWidth - 20);
            if (list.size() == 1) {
                drawCenteredText(context, textRenderer, list.getFirst(), toastWidth / 2, 43, titleColor);
            }
            else {
                int lineHeight = 42 - (9 * (list.size() - 1)) / 2;
                for (OrderedText text : list) {
                    drawCenteredText(context, textRenderer, text, toastWidth / 2, lineHeight, titleColor);
                    lineHeight += 9;
                }
            }
        }

        if (fadeOutProgress > 0) {
            matrix.popMatrix();
        }
    }

    @Override
    public int getDuration() {
        return DURATION;
    }

    @Override
    public int getToastSoundTiming() {
        return TEXT_APPEARANCE.startPoint();
    }
}
