package dev.fabien2s.bookly.mixin.item;

import dev.fabien2s.bookly.util.InstrumentMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(InstrumentItem.class)
public abstract class InstrumentItemMixin {

    @Inject(method = "getInstrument", at = @At("HEAD"), cancellable = true)
    private void getInstrument(ItemStack itemStack, CallbackInfoReturnable<Optional<Holder<Instrument>>> cir) {
        Instrument instrument = InstrumentMap.getInstrument(itemStack);
        if (instrument != null) {
            Holder.Direct<Instrument> instrumentDirect = new Holder.Direct<>(instrument);
            cir.setReturnValue(Optional.of(instrumentDirect));
        }
    }

    @ModifyArg(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), index = 0)
    private static @Nullable Player play(@Nullable Player player) {
        return null;
    }

}
