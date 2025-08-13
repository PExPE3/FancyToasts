package net.bivrik.fancytoasts.config;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.toast.animation.AnimationType;
import net.bivrik.fancytoasts.toast.texture.TextureType;
import net.bivrik.fancytoasts.utilites.TextRendererHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private AnimationType animationType;
    private TextureType textureType;

    public ConfigScreen(Screen parent) {
        super(Text.literal("FancyToastsConfigs"));
        this.parent = parent;
        this.animationType = FancyToasts.CONFIG.animationType;
        this.textureType = FancyToasts.CONFIG.textureType;
    }

    private String getTranslateableEnum(String enumType) {
        return "fancytoasts.gui." + enumType.toLowerCase();
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable(getTranslateableEnum(animationType.toString())),
                button -> {
                    switch (animationType) {
                        case STANDARD -> animationType = AnimationType.PLAYFUL;
                        case PLAYFUL -> animationType = AnimationType.STANDARD;
                    }
                    button.setMessage(Text.translatable(getTranslateableEnum(animationType.toString())));
                }
        ).dimensions(width / 2 - 60, 50, 120, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable(getTranslateableEnum(textureType.toString())),
                button -> {
                    switch (textureType) {
                        case VANILLA -> textureType = TextureType.NATURE;
                        case NATURE -> textureType = TextureType.OG;
                        case OG -> textureType = TextureType.VANILLA;
                    }
                    button.setMessage(Text.translatable(getTranslateableEnum(textureType.toString())));
                }
        ).dimensions(width / 2 - 60, 80, 120, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("fancytoasts.gui.save_changes"),
                (btn) -> {
                    FancyToasts.CONFIG.save(animationType, textureType);
                    openPreviousScreen();
                }
        ).dimensions(width / 2 + 10, height - 40, 120, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("fancytoasts.gui.close"),
                (btn) -> {
                    openPreviousScreen();
                }
        ).dimensions(width / 2 - 130, height - 40, 120, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);

        TextRendererHelper.drawCenteredText(context, this.textRenderer, Text.translatable("fancytoasts.gui.config_title").asOrderedText(), width / 2, 20, Colors.WHITE, true);

        context.drawText(this.textRenderer, Text.translatable("fancytoasts.gui.animation_type"), width / 2 - this.textRenderer.getWidth(Text.translatable("fancytoasts.gui.animation_type")) - 70, 55, Colors.WHITE, true);
        context.drawText(this.textRenderer, Text.translatable("fancytoasts.gui.texture_type"), width / 2 - this.textRenderer.getWidth(Text.translatable("fancytoasts.gui.texture_type")) - 70, 85, Colors.WHITE, true);
    }

    private void openPreviousScreen() {
        assert this.client != null;

        this.client.setScreen(this.parent);
    }
}
