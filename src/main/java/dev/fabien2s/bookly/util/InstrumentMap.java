package dev.fabien2s.bookly.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public final class InstrumentMap {

    public static final String CUSTOM_SOUND_TAG = "CustomSound";
    public static final String CUSTOM_RANGE_TAG = "CustomRange";
    public static final String CUSTOM_DURATION_TAG = "CustomDuration";

    public static final int DEFAULT_DURATION = 140;
    public static final float DEFAULT_RANGE = 256f;

    private InstrumentMap() {
    }

    @Nullable
    public static Instrument getInstrument(ItemStack itemStack) {
        if (!itemStack.is(Items.GOAT_HORN)) return null;

        CompoundTag itemStackTag = itemStack.getTag();
        if (itemStackTag == null) return null;

        String customSound = itemStackTag.getString(CUSTOM_SOUND_TAG);
        if (customSound.isEmpty()) return null;

        ResourceLocation soundLocation = ResourceLocation.tryParse(customSound);
        if (soundLocation == null) return null;

        int duration = DEFAULT_DURATION;
        if (itemStackTag.contains(CUSTOM_DURATION_TAG, Tag.TAG_INT)) {
            duration = itemStackTag.getInt(CUSTOM_DURATION_TAG);
        }

        float range = DEFAULT_RANGE;
        if (itemStackTag.contains(CUSTOM_RANGE_TAG, Tag.TAG_FLOAT)) {
            range = itemStackTag.getFloat(CUSTOM_RANGE_TAG);
        }

        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(soundLocation);
        Holder<SoundEvent> soundHolder = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundEvent);
        return new Instrument(soundHolder, duration, range);
    }

    public static ItemStack createCustom(ResourceLocation soundLocation, int duration, float range) {
        ItemStack itemStack = new ItemStack(Items.GOAT_HORN);

        CompoundTag itemStackTag = itemStack.getOrCreateTag();
        {
            // set sound data
            String soundId = soundLocation.toString();
            itemStackTag.putString(CUSTOM_SOUND_TAG, soundId);
            itemStackTag.putFloat(CUSTOM_RANGE_TAG, range);
            itemStackTag.putInt(CUSTOM_DURATION_TAG, duration);

            // set sound text
            CompoundTag displayTag = new CompoundTag();
            {
                ListTag loreListTag = new ListTag();
                {
                    loreListTag.add(StringTag.valueOf(Component.Serializer.toJson(Component
                            .literal(soundId)
                            .withStyle(ChatFormatting.GRAY)
                    )));
                }
                displayTag.put(ItemStack.TAG_LORE, loreListTag);
            }
            itemStackTag.put(ItemStack.TAG_DISPLAY, displayTag);
        }

        return itemStack;
    }


}
