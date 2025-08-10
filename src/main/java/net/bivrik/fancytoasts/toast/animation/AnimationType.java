package net.bivrik.fancytoasts.toast.animation;

import net.bivrik.fancytoasts.toast.animation.preset.PlayfulAnimation;
import net.bivrik.fancytoasts.toast.animation.preset.StandardAnimation;

import java.util.Map;
import java.util.function.Supplier;

public enum AnimationType {
    STANDARD,
    PLAYFUL;

    public static final Map<AnimationType, Supplier<AdvancementToastAnimation>> ANIMATIONS = Map.of(
            AnimationType.STANDARD, StandardAnimation::new,
            AnimationType.PLAYFUL, PlayfulAnimation::new
    );
}
