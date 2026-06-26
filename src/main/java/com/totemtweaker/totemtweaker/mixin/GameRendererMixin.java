package com.totemtweaker.totemtweaker.mixin;

import com.totemtweaker.totemtweaker.config.TotemTweakerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public abstract class GameRendererMixin {

    @Inject(
        method = "addTotemParticles",
        at = @At("HEAD"),
        cancellable = true,
        require = 0
    )
    private void totemtweaker$cancelTotemParticles(LivingEntity entity, CallbackInfo ci) {
        if (TotemTweakerConfig.get().removeParticles) {
            ci.cancel();
        }
    }
}
