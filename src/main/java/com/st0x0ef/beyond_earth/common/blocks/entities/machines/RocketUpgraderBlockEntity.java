package com.st0x0ef.beyond_earth.common.blocks.entities.machines;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import com.st0x0ef.beyond_earth.common.entities.RocketEntity;
import com.st0x0ef.beyond_earth.common.items.RocketItem;
import com.st0x0ef.beyond_earth.common.items.RocketUpgradeItem;
import com.st0x0ef.beyond_earth.common.menus.RocketUpgraderMenu;
import com.st0x0ef.beyond_earth.common.registries.BlockEntityRegistry;
import com.st0x0ef.beyond_earth.common.registries.ItemsRegistry;
import com.st0x0ef.beyond_earth.common.registries.TagRegistry;

public class RocketUpgraderBlockEntity extends AbstractMachineBlockEntity {

    public static final int SLOT_UPGRADE_INPUT = 0;
    public static final int SLOT_ROCKET_INPUT = 1;
    public static final int SLOT_OUTPUT = 2;

    public RocketUpgraderBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ROCKET_UPGRADER_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    protected boolean onCanPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (index == this.getSlotUpgradeInput()) {
            return stack.is(TagRegistry.ROCKET_UPGRADE_TAG);
        }
        else if (index == this.getSlotRocketInput()) {
            return stack.is(ItemsRegistry.ROCKET_ITEM.get());
        }
        else if (index == this.getSlotOutput()) {
            return false;
        }

        return super.onCanPlaceItemThroughFace(index, stack, direction);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (index == this.getSlotRocketInput() || index == this.getSlotUpgradeInput()) {
            return false;
        } else if (index == this.getSlotOutput()) {
            return true;
        }

        return super.canTakeItemThroughFace(index, stack, direction);
    }

    @Override
    protected void tickProcessing() {
        ItemStack upgrade_input = this.getItem(getSlotUpgradeInput());
        ItemStack rocket_input = this.getItem(getSlotRocketInput());

        if (!upgrade_input.isEmpty() && !rocket_input.isEmpty() && hasSpaceInOutput()) {
            ItemStack output = rocket_input.copy();

            if (output.getItem() instanceof RocketItem rocket) {
                if (upgrade_input.getItem() instanceof RocketUpgradeItem upgrade) {
                    rocket.fuelCapacityModifier = upgrade.getFuelCapacityModifier() > 0 ? upgrade.getFuelCapacityModifier() : rocket.fuelCapacityModifier;
                    rocket.fuelUsageModifier = upgrade.getFuelUsageModifier() > 0 ? upgrade.getFuelUsageModifier() : rocket.fuelUsageModifier;
                    output = rocket.asItem().getDefaultInstance();

                    output.getOrCreateTag().putInt("fuelCapacityModifier", rocket.fuelCapacityModifier);
                    output.getOrCreateTag().putInt("fuelUsageModifier", rocket.fuelUsageModifier);

                    this.removeItem(getSlotUpgradeInput(), 1);
                    this.removeItem(getSlotRocketInput(), 1);
                    this.setItem(getSlotOutput(), output);
                }
            }
        }
     }

    @Override
    public boolean hasSpaceInOutput() {
        return this.getItem(getSlotOutput()).isEmpty();
    }

    @Override
    protected void getSlotsForFace(Direction direction, List<Integer> slots) {
        super.getSlotsForFace(direction, slots);
        slots.add(this.getSlotRocketInput());
        slots.add(this.getSlotUpgradeInput());
        slots.add(this.getSlotOutput());
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new RocketUpgraderMenu.GuiContainer(id, inventory, this);
    }

    @Override
    protected int getInitialInventorySize() {
        return super.getInitialInventorySize() + 3;
    }

    public int getSlotRocketInput() {
        return SLOT_ROCKET_INPUT;
    }
    public int getSlotUpgradeInput() {
        return SLOT_UPGRADE_INPUT;
    }
    public int getSlotOutput() {
        return SLOT_OUTPUT;
    }
}
