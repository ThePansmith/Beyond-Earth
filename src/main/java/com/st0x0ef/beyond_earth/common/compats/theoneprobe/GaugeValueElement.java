package com.st0x0ef.beyond_earth.common.compats.theoneprobe;

import mcjty.theoneprobe.api.IElement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import com.st0x0ef.beyond_earth.client.screens.helper.gauge.GaugeValueRenderer;
import com.st0x0ef.beyond_earth.common.blocks.entities.machines.gauge.IGaugeValue;

public class GaugeValueElement extends GaugeValueRenderer implements IElement {

	public GaugeValueElement(IGaugeValue value) {
		super(value);
	}

	public GaugeValueElement(FriendlyByteBuf buffer) {
		super(buffer);
	}

	@Override
	public ResourceLocation getID() {
		return GaugeValueElementFactory.ELEMENT_ID;
	}

}
