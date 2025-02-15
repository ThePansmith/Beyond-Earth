package com.st0x0ef.beyond_earth.client.screens.helper.gauge;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import com.st0x0ef.beyond_earth.BeyondEarth;
import com.st0x0ef.beyond_earth.client.util.GuiHelper;
import com.st0x0ef.beyond_earth.common.blocks.entities.machines.gauge.GaugeValueFluidStack;
import com.st0x0ef.beyond_earth.common.blocks.entities.machines.gauge.GaugeValueSerializer;
import com.st0x0ef.beyond_earth.common.blocks.entities.machines.gauge.GaugeValueSimple;
import com.st0x0ef.beyond_earth.common.blocks.entities.machines.gauge.IGaugeValue;

public class GaugeValueRenderer extends AbstractGaugeDataRenderer {

    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(BeyondEarth.MODID,
            "textures/simplegaugevalue.png");

    public GaugeValueRenderer(IGaugeValue value) {
        super(value);
    }

    public GaugeValueRenderer(FriendlyByteBuf buffer) {
        super(GaugeValueSerializer.Serializer.read(buffer));
    }

    @Override
    public TextureAtlasSprite getBackgroundTileTexture() {
        IGaugeValue value = this.getValue();

        if (value instanceof GaugeValueFluidStack) {
            return GuiHelper.getStillFluidSprite(((GaugeValueFluidStack) value).getStack());
        } else {
            return null;
        }
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        IGaugeValue value = this.getValue();
        return value instanceof GaugeValueSimple ? DEFAULT_TEXTURE : null;
    }

}
