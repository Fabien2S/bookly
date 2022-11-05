package dev.fabien2s.bookly.mixin.player;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Redirect(method = "setViewDistance", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void setViewDistance(PlayerList instance, Packet<?> packet) {
        // prevent setViewDistance to send packet
    }

}
