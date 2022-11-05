package dev.fabien2s.bookly.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public final class InstrumentMap {

    public static final String CUSTOM_SOUND_TAG = "CustomSound";

    private InstrumentMap() {
    }

    @Nullable
    public static ResourceLocation getCustomSound(ItemStack itemStack) {
        if (!itemStack.is(Items.GOAT_HORN)) return null;

        CompoundTag itemStackTag = itemStack.getTag();
        if (itemStackTag == null) return null;

        String customSound = itemStackTag.getString(CUSTOM_SOUND_TAG);
        return customSound.isEmpty() ? null : ResourceLocation.tryParse(customSound);
    }

    public static ItemStack createCustom(ResourceLocation soundLocation) {
        ItemStack itemStack = new ItemStack(Items.GOAT_HORN);

        CompoundTag itemStackTag = itemStack.getOrCreateTag();
        {
            // display as golden
            itemStackTag.putInt("CustomModelData", 1);

            // set sound effect
            String soundId = soundLocation.toString();
            itemStackTag.putString(CUSTOM_SOUND_TAG, soundId);

            // set sound text
            CompoundTag displayTag = new CompoundTag();
            {
                ListTag loreListTag = new ListTag();
                {
                    loreListTag.add(StringTag.valueOf(Component.Serializer.toJson(Component
                            .literal(soundId)
                            .withStyle(ChatFormatting.GRAY))
                    ));
                }
                displayTag.put(ItemStack.TAG_LORE, loreListTag);
            }
            itemStackTag.put(ItemStack.TAG_DISPLAY, displayTag);
        }

        return itemStack;
    }

    public static Holder<Instrument> getInstrument(ResourceLocation soundLocation) {
        return new Holder.Direct<>(new Instrument(new SoundEvent(soundLocation), 140, 256.0f));
    }


}
