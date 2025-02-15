package com.st0x0ef.beyond_earth.common.compats.waila;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import com.st0x0ef.beyond_earth.client.screens.helper.gauge.GaugeValueRenderer;
import snownee.jade.api.ui.Element;

public class GaugeValueElement extends Element {

	public static final int RIGHT_PADDING = 2;
	public static final int TOP_OFFSET = 1;
	public static final int BOTTOM_PADDING = 1;

	private final GaugeValueRenderer renderer;

	public GaugeValueElement(GaugeValueRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public Vec2 getSize() {
		Vec2 size = this.getRenderer().getSize();
		return new Vec2(size.x, size.y + TOP_OFFSET);
	}

	@Override
	public void render(GuiGraphics graphics, float x, float y, float maxX, float maxY) {
		this.getRenderer().render(graphics, (int) x, (int) y, (int) (maxX - x - RIGHT_PADDING), (int) (maxY - y - TOP_OFFSET - BOTTOM_PADDING));
	}

	public GaugeValueRenderer getRenderer() {
		return this.renderer;
	}

}
