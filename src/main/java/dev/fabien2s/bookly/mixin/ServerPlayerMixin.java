package dev.fabien2s.bookly.mixin;

import dev.fabien2s.bookly.BooklyMod;
import dev.fabien2s.bookly.player.ServerPlayerExtra;
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
public class ServerPlayerMixin implements ServerPlayerExtra {

    @Shadow
    public ServerGamePacketListenerImpl connection;

    private int viewDistance;

    @Inject(method = "updateOptions", at = @At("HEAD"))
    private void updateOptions(ServerboundClientInformationPacket packet, CallbackInfo ci) {
        this.viewDistance = packet.viewDistance();
        this.connection.send(new ClientboundSetChunkCacheRadiusPacket(this.viewDistance));
        BooklyMod.LOGGER.debug("Sending adjusted view distance of {} to {}", this.viewDistance, this);
    }

    @Override
    public int getViewDistance() {
        return this.viewDistance;
    }

}
