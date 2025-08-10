package net.bivrik.fancytoasts.toast.texture;

import net.bivrik.fancytoasts.FancyToasts;
import net.minecraft.util.Identifier;

import java.util.Map;

public enum TextureType {
    VANILLA,
    NATURE,
    OG;

    public static final Identifier TOASTS_TEXTURE_ATLAS = Identifier.of(FancyToasts.MOD_ID, "textures/gui/advancement_toasts.png");
    public static final Identifier NATURE_TOASTS_TEXTURE_ATLAS = Identifier.of(FancyToasts.MOD_ID, "textures/gui/nature_advancement_toasts.png");
    public static final Identifier OG_TOASTS_TEXTURE_ATLAS = Identifier.of(FancyToasts.MOD_ID, "textures/gui/og_advancement_toasts.png");

    public static final Map<TextureType, Identifier> TEXTURES = Map.of(
            TextureType.VANILLA, TOASTS_TEXTURE_ATLAS,
            TextureType.NATURE, NATURE_TOASTS_TEXTURE_ATLAS,
            TextureType.OG, OG_TOASTS_TEXTURE_ATLAS
    );
}
