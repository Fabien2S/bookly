package dev.fabien2s.bookly.mixin.level;

import net.minecraft.core.Registry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @ModifyArg(
            method = "playSeededSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFJ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcast(Lnet/minecraft/world/entity/player/Player;DDDDLnet/minecraft/resources/ResourceKey;Lnet/minecraft/network/protocol/Packet;)V"),
            index = 6
    )
    private Packet<?> playSeededSoundFromPosition(Packet<?> packet) {
        ClientboundSoundPacket soundPacket = (ClientboundSoundPacket) packet;
        SoundEvent soundEvent = soundPacket.getSound();
        int id = Registry.SOUND_EVENT.getId(soundEvent);
        if (id != -1) return packet;

        return new ClientboundCustomSoundPacket(
                soundEvent.getLocation(),
                soundPacket.getSource(),
                new Vec3(
                        soundPacket.getX(),
                        soundPacket.getY(),
                        soundPacket.getZ()
                ),
                soundPacket.getVolume(),
                soundPacket.getPitch(),
                soundPacket.getSeed()
        );
    }

    @ModifyArgs(
            method = "playSeededSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFJ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcast(Lnet/minecraft/world/entity/player/Player;DDDDLnet/minecraft/resources/ResourceKey;Lnet/minecraft/network/protocol/Packet;)V")
    )
    private void playSeededSoundFromEntity(Args args) {
        ClientboundSoundEntityPacket soundPacket = args.get(6);
        SoundEvent soundEvent = soundPacket.getSound();
        int id = Registry.SOUND_EVENT.getId(soundEvent);
        if (id != -1) return;

        args.set(6, new ClientboundCustomSoundPacket(
                soundEvent.getLocation(),
                soundPacket.getSource(),
                new Vec3(
                        args.get(1),
                        args.get(2),
                        args.get(3)
                ),
                soundPacket.getVolume(),
                soundPacket.getPitch(),
                soundPacket.getSeed()
        ));
    }

}
