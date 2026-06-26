package com.totemtweaker.totemtweaker.client;

import com.totemtweaker.totemtweaker.config.TotemTweakerConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class TotemTweakerClient implements ClientModInitializer {

    public static final String MOD_ID = "totemtweaker";

    @Override
    public void onInitializeClient() {
        TotemTweakerConfig.get();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(
                ClientCommandManager.literal("totemtweaker")
                    .then(ClientCommandManager.literal("reload")
                        .executes(ctx -> {
                            TotemTweakerConfig.reload();
                            ctx.getSource().sendFeedback(
                                Text.literal("[TotemTweaker] Config reloaded!")
                            );
                            return 1;
                        })
                    )
                    .then(ClientCommandManager.literal("info")
                        .executes(ctx -> {
                            TotemTweakerConfig cfg = TotemTweakerConfig.get();
                            ctx.getSource().sendFeedback(Text.literal(
                                "[TotemTweaker] scale=" + cfg.animationScale
                                + " | alpha=" + cfg.animationAlpha
                                + " | removeParticles=" + cfg.removeParticles
                                + " | handScale=" + cfg.handScalePercent + "%"
                            ));
                            return 1;
                        })
                    )
            )
        );
    }
}
