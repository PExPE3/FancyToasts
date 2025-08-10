package net.bivrik.fancytoasts.toast.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TextureUV {
    private final int u;
    private final int v;

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    private TextureUV(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public static final TextureUV[] TASK_TEXTURE_UV = {
            new TextureUV(0, 40),
            new TextureUV(0, 82)
    };
    public static final TextureUV[] GOAL_TEXTURE_UV = {
            new TextureUV(0, 54),
            new TextureUV(26, 82)
    };
    public static final TextureUV[] CHALLENGE_TEXTURE_UV = {
            new TextureUV(0, 68),
            new TextureUV(52, 82)
    };
}
