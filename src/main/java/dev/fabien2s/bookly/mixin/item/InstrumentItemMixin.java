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
    private void use(ItemStack itemStack, CallbackInfoReturnable<Optional<Holder<Instrument>>> cir) {
        ResourceLocation soundLocation = InstrumentMap.getCustomSound(itemStack);
        if (soundLocation == null) {
            return;
        }

        Holder<Instrument> instrument = InstrumentMap.getInstrument(soundLocation);
        cir.setReturnValue(Optional.of(instrument));
    }

    @ModifyArgs(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private static void play(Args args) {
        SoundEvent soundEvent = args.get(2);
        int id = Registry.SOUND_EVENT.getId(soundEvent);
        if (id == -1) args.set(0, null);
    }

}
