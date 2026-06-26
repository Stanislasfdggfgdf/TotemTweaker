package com.totemtweaker.totemtweaker.mixin;

import com.totemtweaker.totemtweaker.config.TotemTweakerConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class TotemOverlayRendererMixin {

    private static final String TOTEM_TEXTURE_PATH = "totem_of_undying";

    @Inject(
        method = "renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void totemtweaker$onRenderOverlay(DrawContext context, Identifier texture, float opacity, CallbackInfo ci) {
        if (!texture.getPath().contains(TOTEM_TEXTURE_PATH)) return;

        TotemTweakerConfig cfg = TotemTweakerConfig.get();
        if (cfg.animationScale >= 1.0f && cfg.animationAlpha >= 1.0f) return;

        ci.cancel();

        float scale = Math.max(0f, Math.min(1f, cfg.animationScale));
        float alpha = Math.max(0f, Math.min(1f, cfg.animationAlpha)) * opacity;

        if (alpha <= 0f || scale <= 0f) return;

        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth  = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int drawW = (int)(screenWidth  * scale);
        int drawH = (int)(screenHeight * scale);
        int x     = (screenWidth  - drawW) / 2;
        int y     = (screenHeight - drawH) / 2;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);

        context.drawTexture(texture, x, y, 0, 0, drawW, drawH, drawW, drawH);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }
}
