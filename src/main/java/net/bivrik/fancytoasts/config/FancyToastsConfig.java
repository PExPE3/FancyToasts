package net.bivrik.fancytoasts.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.toast.animation.AnimationType;
import net.bivrik.fancytoasts.toast.texture.TextureType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FancyToastsConfig {
    public AnimationType animationType;
    public TextureType textureType;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("./config/" + FancyToasts.MOD_ID + ".json");

    public static FancyToastsConfig load() {
        if (CONFIG_FILE.exists() && CONFIG_FILE.length() > 0) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                var json = GSON.fromJson(reader, FancyToastsConfig.class);
                FancyToasts.LOGGER.info(FancyToasts.MOD_ID + " config file is loaded.");
                return json;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        FancyToastsConfig config = new FancyToastsConfig();
        config.save(AnimationType.STANDARD, TextureType.VANILLA);
        FancyToasts.LOGGER.info(FancyToasts.MOD_ID + " config file created.");
        return config;
    }

    public void save(AnimationType animationType, TextureType textureType) {
        this.animationType = animationType;
        this.textureType = textureType;

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
            FancyToasts.LOGGER.info(FancyToasts.MOD_ID + " config file saved.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
