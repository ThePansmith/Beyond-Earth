package com.st0x0ef.beyond_earth.common.world.processors;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import com.st0x0ef.beyond_earth.common.registries.StructureProcessorRegistry;

public class StructureVoidProcessor extends StructureProcessor {

    public static final Codec<StructureVoidProcessor> CODEC = Codec.unit(StructureVoidProcessor::new);

    private StructureVoidProcessor() {

    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (structureBlockInfoWorld.state().is(Blocks.STRUCTURE_VOID)) {
            return null;
        }

        if (worldView.getBlockState(structureBlockInfoWorld.pos()).isAir()) {
            return null;
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.STRUCTURE_VOID_PROCESSOR;
    }
}