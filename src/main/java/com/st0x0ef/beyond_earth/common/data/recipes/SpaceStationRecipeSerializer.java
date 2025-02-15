package com.st0x0ef.beyond_earth.common.data.recipes;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class SpaceStationRecipeSerializer extends BeyondEarthRecipeSerializer<SpaceStationRecipe> {

	@Override
	public SpaceStationRecipe fromJson(ResourceLocation id, JsonObject json) {
		return new SpaceStationRecipe(id, json);
	}

	@Override
	public SpaceStationRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
		return new SpaceStationRecipe(id, buffer);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, SpaceStationRecipe recipe) {
		recipe.write(buffer);
	}

}
