package dev.fabien2s.bookly.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.fabien2s.bookly.util.InstrumentMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class HornCommand {

    private HornCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("horn")
                .requires(ctx -> ctx.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(argument("sound", ResourceLocationArgument.id()).suggests(SuggestionProviders.AVAILABLE_SOUNDS)
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            ServerPlayer player = source.getPlayerOrException();

                            ResourceLocation soundLocation = ResourceLocationArgument.getId(context, "sound");
                            ItemStack itemStack = InstrumentMap.createCustom(soundLocation, InstrumentMap.DEFAULT_DURATION, InstrumentMap.DEFAULT_RANGE);
                            return player.addItem(itemStack) ? Command.SINGLE_SUCCESS : 0;
                        })
                        .then(argument("duration", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    ServerPlayer player = source.getPlayerOrException();

                                    ResourceLocation soundLocation = ResourceLocationArgument.getId(context, "sound");
                                    int duration = IntegerArgumentType.getInteger(context, "duration");
                                    ItemStack itemStack = InstrumentMap.createCustom(soundLocation, duration, InstrumentMap.DEFAULT_RANGE);
                                    return player.addItem(itemStack) ? Command.SINGLE_SUCCESS : 0;
                                })
                                .then(argument("range", FloatArgumentType.floatArg(16))
                                        .executes(context -> {
                                            CommandSourceStack source = context.getSource();
                                            ServerPlayer player = source.getPlayerOrException();

                                            ResourceLocation soundLocation = ResourceLocationArgument.getId(context, "sound");
                                            int duration = IntegerArgumentType.getInteger(context, "duration");
                                            float range = FloatArgumentType.getFloat(context, "range");
                                            ItemStack itemStack = InstrumentMap.createCustom(soundLocation, duration, range);
                                            return player.addItem(itemStack) ? Command.SINGLE_SUCCESS : 0;
                                        })
                                )
                        )
                )
        );
    }

}
