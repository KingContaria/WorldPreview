package me.voidxwalker.worldpreview.mixin.other;

import me.voidxwalker.worldpreview.WorldPreview;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.WorldGenerationProgressLogger;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.ChunkStatus;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldGenerationProgressLogger.class)
public abstract class WorldGenerationProgressLoggerMixin {

    private static final Logger LOGGER = WorldPreview.LOGGER;
    @Shadow public abstract int getProgressPercentage();

    @Inject(method = "setChunkStatus", at = @At("TAIL"))
    private void checkForFreeze(ChunkPos pos, ChunkStatus status, CallbackInfo ci){
        int i = this.getProgressPercentage();
        if(MathHelper.clamp(i, 0, 100) >= WorldPreview.autoFreezeAt && !WorldPreview.freezePreview && WorldPreview.autoFreezeAt != 100){
            LOGGER.info("Automatically froze Preview at " + i + "%");
            WorldPreview.freezePreview = true;
        }
    }

}
