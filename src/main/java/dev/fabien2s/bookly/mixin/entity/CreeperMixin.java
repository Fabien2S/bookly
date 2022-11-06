package dev.fabien2s.bookly.mixin.entity;

import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Creeper.class)
public class CreeperMixin {

    @ModifyArg(
            method = "explodeCreeper",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Explosion$BlockInteraction;)Lnet/minecraft/world/level/Explosion;"
            ),
            index = 5
    )
    private Explosion.BlockInteraction d(Explosion.BlockInteraction value) {
        return value == Explosion.BlockInteraction.DESTROY ? Explosion.BlockInteraction.BREAK : value;
    }

}