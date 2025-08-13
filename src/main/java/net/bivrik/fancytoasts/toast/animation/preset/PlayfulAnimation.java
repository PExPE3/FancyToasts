package net.bivrik.fancytoasts.toast.animation.preset;

import net.bivrik.fancytoasts.toast.animation.AdvancementToastAnimation;
import net.bivrik.fancytoasts.utilites.EasingHelper;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.List;

import static net.bivrik.fancytoasts.utilites.TextRendererHelper.drawCenteredText;

public class PlayfulAnimation extends AdvancementToastAnimation {
    private static final Appearance ICON_APPEARANCE = new Appearance(1000, 0);
    private static final Appearance ICON_MOVEMENT = new Appearance(1500, 1000);
    private static final Appearance BANNER_APPEARANCE = new Appearance(1000, 1500);
    private static final Appearance BACKGROUND_APPEARANCE = new Appearance(800, 1600);
    private static final Appearance TEXT_APPEARANCE = new Appearance(1000, 2000);

    private static final int FADE_OUT_DURATION = 1500;
    private static final int DURATION = 6000 + FADE_OUT_DURATION;

    @Override
    public void draw(DrawContext context, TextRenderer textRenderer, long time, int toastWidth, int toastHeight) {
        float iconAppearProgress = getProgress(time, ICON_APPEARANCE);
        float iconMovementProgress = getProgress(time, ICON_MOVEMENT);
        float bannerAppearProgress = getProgress(time, BANNER_APPEARANCE);
        float backgroundAppearProgress = getProgress(time, BACKGROUND_APPEARANCE);
        float textAppearProgress = getProgress(time, TEXT_APPEARANCE);
        float fadeOutProgress = getProgress(time, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

        var matrix = context.getMatrices();
        float sinY = (float) (Math.sin(time / 400.0)) - 3;

        if (fadeOutProgress > 0) {
            float fadeOutScale = EasingHelper.easeInLerp(1f, 0f, fadeOutProgress);

            matrix.push();
            matrix.translate((float) toastWidth / 2, (float) toastHeight / 2, 0);
            matrix.scale(fadeOutScale, fadeOutScale, fadeOutScale);
            matrix.translate((float) toastWidth / -2, (float) toastHeight / -2, 0);
        }

        if (backgroundAppearProgress > 0) {
            matrix.push();
            if (backgroundAppearProgress != 1) {
                float scale = EasingHelper.elasticEaseOutLerp(0f, 1f, backgroundAppearProgress);
                float rotation = EasingHelper.elasticEaseOutLerp(-1f, 0f, backgroundAppearProgress);
                int y = EasingHelper.easeOutLerp(-20, 0, backgroundAppearProgress);

                matrix.translate(76, 0, 0);
                matrix.scale(scale, scale, scale);
                matrix.multiply(RotationAxis.POSITIVE_Z.rotation(rotation));
                matrix.translate(-76, 0, 0);
                matrix.translate(0, y, 0);
            }
            drawBackground(context);
            matrix.pop();
        }

        if (bannerAppearProgress > 0) {
            matrix.push();
            if (bannerAppearProgress != 1) {
                float scaleX = EasingHelper.easeOutLerp(0f, 1f, bannerAppearProgress);

                matrix.translate(15, -5 - 7, 0);
                matrix.scale(scaleX, 1, 1);
                matrix.translate(-15, 5 + 7, 0);
            }
            matrix.translate(0, sinY, 0);
            drawBanner(context);
            matrix.pop();
        }

        if (iconAppearProgress > 0) {
            matrix.push();
            if (iconAppearProgress != 1) {
                float scale = EasingHelper.elasticEaseOutLerp(0f, 1f, iconAppearProgress);
                float rotation = EasingHelper.elasticEaseOutLerp(-1f, 0f, iconAppearProgress);

                matrix.translate(68 + 13, 13, 0);
                matrix.scale(scale, scale, scale);
                matrix.multiply(RotationAxis.POSITIVE_Z.rotation(rotation));
                matrix.translate(-68 - 13, -13, 0);
            }
            else if (iconMovementProgress > 0) {
                float x = EasingHelper.easeOutLerp(0f, -60f, iconMovementProgress);

                matrix.translate(x, 0, 0);
            }
            matrix.translate(0, sinY - 5, 0);
            drawIcon(context);
            matrix.pop();
        }

        if (textAppearProgress > 0) {
            int a = MathHelper.floor(textAppearProgress * 255.0F) << 24 | 67108864;

            // Title
            List<OrderedText> titleList = textRenderer.wrapLines(getSetup().display().getTitle(), toastWidth - 16);
            if (titleList.size() == 1) {
                drawCenteredText(context, textRenderer, titleList.get(0), toastWidth / 2, 25, getSetup().toastColor() | a);
            }
            else {
                drawCenteredText(context, textRenderer, titleList.get(0), toastWidth / 2 - textRenderer.getWidth("...") / 2, 25, getSetup().toastColor() | a);
                drawCenteredText(context, textRenderer, Text.literal("...").asOrderedText(), 1 + toastWidth / 2 + textRenderer.getWidth(titleList.get(0)) / 2, 25, getSetup().toastColor() | a);
            }

            // Description
            List<OrderedText> descriptionList = textRenderer.wrapLines(getSetup().display().getDescription(), toastWidth - 16);
            if (descriptionList.size() == 1) {
                context.drawText(textRenderer, descriptionList.get(0), 8, 38, getSetup().titleColor() | a, false);
            }
            else {
                context.drawText(textRenderer, descriptionList.get(0), 8, 38, getSetup().titleColor() | a, false);
                context.drawText(textRenderer, descriptionList.get(1), 8, 47, getSetup().titleColor() | a, false);
                if (descriptionList.size() > 2) {
                    context.drawText(textRenderer, Text.literal("..."), 8 + textRenderer.getWidth(descriptionList.get(1)), 47, getSetup().titleColor() | a, false);
                }
            }
        }

        if (fadeOutProgress > 0) {
            matrix.pop();
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
