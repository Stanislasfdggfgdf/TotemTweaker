package com.totemtweaker.totemtweaker.mixin;

import com.totemtweaker.totemtweaker.config.TotemTweakerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemRenderer.class)
public abstract class LivingEntityRendererMixin {

    private boolean totemtweaker$didPush = false;

    @Inject(
        method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At("HEAD")
    )
    private void totemtweaker$scaleTotemPre(
            AbstractClientPlayerEntity player, float tickDelta, float pitch,
            Hand hand, float swingProgress, ItemStack item, float equipProgress,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            CallbackInfo ci) {

        totemtweaker$didPush = false;
        if (item.isOf(Items.TOTEM_OF_UNDYING)) {
            float s = TotemTweakerConfig.get().handScaleMultiplier();
            if (s < 0.9999f) {
                matrices.push();
                matrices.scale(s, s, s);
                totemtweaker$didPush = true;
            }
        }
    }

    @Inject(
        method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At("RETURN")
    )
    private void totemtweaker$scaleTotemPost(
            AbstractClientPlayerEntity player, float tickDelta, float pitch,
            Hand hand, float swingProgress, ItemStack item, float equipProgress,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            CallbackInfo ci) {

        if (totemtweaker$didPush) {
            matrices.pop();
            totemtweaker$didPush = false;
        }
    }
}
