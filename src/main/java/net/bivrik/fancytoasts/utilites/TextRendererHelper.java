package net.bivrik.fancytoasts.utilites;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;

public class TextRendererHelper {
    public static void drawCenteredText(DrawContext context, TextRenderer textRenderer, OrderedText text, int x, int y, int color, boolean shadow) {
        int centeredX = x - textRenderer.getWidth(text) / 2;
        context.drawText(textRenderer, text, centeredX, y, color, shadow);
    }
    public static void drawCenteredText(DrawContext context, TextRenderer textRenderer, OrderedText text, int x, int y, int color) {
        drawCenteredText(context, textRenderer, text, x, y, color, false);
    }
}
