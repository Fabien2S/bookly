package dev.fabien2s.bookly.mixin.player;

import dev.fabien2s.bookly.BooklyMod;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Shadow
    public ServerGamePacketListenerImpl connection;

    @Inject(method = "updateOptions", at = @At("HEAD"))
    private void updateOptions(ServerboundClientInformationPacket packet, CallbackInfo ci) {
        int viewDistance = packet.viewDistance();
        this.connection.send(new ClientboundSetChunkCacheRadiusPacket(viewDistance));
        BooklyMod.LOGGER.debug("Sending adjusted view distance of {} to {}", viewDistance, this);
    }

}
