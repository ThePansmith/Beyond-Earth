package com.st0x0ef.beyond_earth.common.registries;

import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import com.st0x0ef.beyond_earth.BeyondEarth;

@Mod.EventBusSubscriber(modid = BeyondEarth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FluidInteractRegistry {

    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            addInteraction(FluidTypeRegistry.FUEL_TYPE.get());
            addInteraction(FluidTypeRegistry.OIL_TYPE.get());
            addInteraction(FluidTypeRegistry.HYDROGEN_TYPE.get());
        });
    }

    // Lava + Water = Obsidian (Source Lava) / Cobblestone (Flowing Lava)
    private static void addInteraction(FluidType fluidType) {
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(fluidType, fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState()));
    }
}
