package com.st0x0ef.beyond_earth.common.registries;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
import net.minecraftforge.registries.*;
import com.st0x0ef.beyond_earth.BeyondEarth;
import com.st0x0ef.beyond_earth.common.features.InfernalSpireColumn;
import com.st0x0ef.beyond_earth.common.features.ModifiedBlockBlobFeature;

public class FeatureRegistry {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, BeyondEarth.MODID);

    /**
     * FEATURES
     */
    public static final RegistryObject<InfernalSpireColumn> INFERNAL_SPIRE_COLUMN = FEATURES.register("infernal_spire_column", () -> new InfernalSpireColumn(ColumnFeatureConfiguration.CODEC));
    public static final RegistryObject<ModifiedBlockBlobFeature> MODIFIED_BLOCK_BLOB = FEATURES.register("modified_block_blob", () -> new ModifiedBlockBlobFeature(BlockStateConfiguration.CODEC));


}