package com.flemmli97.runecraftory.common.inventory.container;

import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class UpgradeOutputSlot extends Slot {

    private int amountCrafted;
    private final PlayerContainerInv ingredientInv;
    private final ContainerUpgrade container;

    public UpgradeOutputSlot(IInventory output, ContainerUpgrade container, PlayerContainerInv ingredientInv, int id, int x, int y) {
        super(output, id, x, y);
        this.ingredientInv = ingredientInv;
        this.container = container;
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        PlayerEntity player = this.ingredientInv.getPlayer();
        if (this.amountCrafted > 0) {
            stack.onCrafting(player.world, player, this.amountCrafted);
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(player, stack, this.inventory);
        }
        this.amountCrafted = 0;
    }

    @Override
    protected void onSwapCraft(int amount) {
        super.onSwapCraft(amount);
        this.amountCrafted += amount;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeStack(PlayerEntity player) {
        return ItemUtils.canUpgrade(player, this.container.craftingType(), this.ingredientInv.getStackInSlot(6), this.ingredientInv.getStackInSlot(7));
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().getCount());
        }
        return super.decrStackSize(amount);
    }

    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
        this.onCrafting(stack);
        ItemStack ing1 = this.ingredientInv.getStackInSlot(6);
        ItemStack ing2 = this.ingredientInv.getStackInSlot(7);
        ItemStack ing1f = ing1;
        ItemStack ing2f = ing2;
        player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> cap.decreaseRunePoints(player, ItemUtils.upgradeCost(this.container.craftingType(), player, cap, ing1f, ing2f), true));
        if (!ing1.isEmpty()) {
            this.ingredientInv.decrStackSize(6, 1);
            ing1 = this.ingredientInv.getStackInSlot(6);
        }
        if (!ing2.isEmpty()) {
            this.ingredientInv.decrStackSize(7, 1);
            ing2 = this.ingredientInv.getStackInSlot(7);
        }
        player.world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1, 1);
        if (ing1.isEmpty() || ing2.isEmpty())
            this.container.onCraftMatrixChanged(this.ingredientInv);
        return stack;
    }
}