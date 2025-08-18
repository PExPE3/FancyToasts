package net.bivrik.fancytoasts.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class GUIHelper {
    public static void translate(PoseStack pose, float x, float y, float z) {
        pose.translate(x, y, z);
    }
    public static void translate(PoseStack pose, float x, float y) {
        translate(pose, x, y, 0);
    }

    public static void scale(PoseStack pose, float x, float y) {
        pose.scale(x, y, 1);
    }
    public static void scale(PoseStack pose, float scale) {
        pose.scale(scale, scale, scale);
    }

    public static void rotate(PoseStack pose, float rotation) {
        pose.mulPose(Axis.ZP.rotation(rotation));
    }

    public static PoseStack get(GuiGraphics gui) {
        return gui.pose();
    }

    public static void push(PoseStack pose) {
        pose.pushPose();
    }

    public static void pop(PoseStack pose) {
        pose.popPose();
    }

    public static void drawGUITexture(GuiGraphics graphics, ResourceLocation atlas, int x, int y, int u, int v, int width, int height) {
        graphics.blit(atlas, x, y, u, v, width, height, 256, 256);
    }
}
