package net.bivrik.fancytoasts.toast;

import net.bivrik.fancytoasts.toast.texture.TextureUV;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.util.Identifier;

public record RenderSetup(Identifier texture, TextureUV[] uv, AdvancementDisplay display, int toastColor, int titleColor) {}
