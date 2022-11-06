package dev.fabien2s.bookly.mixin.item;

import dev.fabien2s.bookly.util.InstrumentMap;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

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

    @ModifyArgs(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private static void play(Args args) {
        SoundEvent soundEvent = args.get(2);
        ResourceLocation soundLocation = soundEvent.getLocation();
        if (Registry.SOUND_EVENT.containsKey(soundLocation)) return;

        args.set(0, null);
    }

}
