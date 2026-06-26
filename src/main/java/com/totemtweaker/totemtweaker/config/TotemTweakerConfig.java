package com.totemtweaker.totemtweaker.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configuration for TotemTweaker.
 * Config file: config/totemtweaker.json
 *
 *  - animationScale      : float [0.0–1.0] — shrinks the flash/overlay (1.0 = vanilla)
 *  - animationAlpha      : float [0.0–1.0] — transparency of the overlay (1.0 = fully opaque)
 *  - removeParticles     : boolean          — suppress totem particles on activation
 *  - handScalePercent    : float [1–100]    — scale of the totem in first-person hand (100 = vanilla)
 */
public class TotemTweakerConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("totemtweaker.json");

    private static TotemTweakerConfig instance;

    public float animationScale   = 1.0f;
    public float animationAlpha   = 1.0f;
    public boolean removeParticles = false;
    public float handScalePercent = 100.0f;

    public static TotemTweakerConfig get() {
        if (instance == null) instance = load();
        return instance;
    }

    private static TotemTweakerConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                TotemTweakerConfig cfg = GSON.fromJson(json, TotemTweakerConfig.class);
                cfg.clamp();
                return cfg;
            } catch (IOException | com.google.gson.JsonSyntaxException e) {
                System.err.println("[TotemTweaker] Failed to read config, using defaults: " + e.getMessage());
            }
        }
        TotemTweakerConfig defaults = new TotemTweakerConfig();
        defaults.save();
        return defaults;
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) {
            System.err.println("[TotemTweaker] Failed to save config: " + e.getMessage());
        }
    }

    public static void reload() {
        instance = load();
    }

    private void clamp() {
        animationScale   = clampF(animationScale,   0.0f, 1.0f);
        animationAlpha   = clampF(animationAlpha,   0.0f, 1.0f);
        handScalePercent = clampF(handScalePercent, 1.0f, 100.0f);
    }

    private static float clampF(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }

    public float handScaleMultiplier() {
        return handScalePercent / 100.0f;
    }
}
