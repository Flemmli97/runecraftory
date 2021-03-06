package com.flemmli97.runecraftory.common.inventory.container;

import com.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CraftingOutputSlot extends Slot {

    private int amountCrafted;
    private final PlayerContainerInv ingredientInv;
    private final ContainerCrafting container;

    public CraftingOutputSlot(IInventory output, ContainerCrafting container, PlayerContainerInv ingredientInv, int id, int x, int y) {
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
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().getCount());
        }
        return super.decrStackSize(amount);
    }

    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
        this.onCrafting(stack);
        NonNullList<ItemStack> remaining = this.container.getCurrentRecipe() != null ? this.container.getCurrentRecipe().getRemainingItems(this.ingredientInv) : NonNullList.withSize(0, ItemStack.EMPTY);
        boolean refreshRecipe = false;
        for (int i = 0; i < remaining.size(); ++i) {
            ItemStack itemstack = this.ingredientInv.getStackInSlot(i);
            ItemStack remainingStack = remaining.get(i);
            if (!itemstack.isEmpty()) {
                this.ingredientInv.decrStackSize(i, 1);
                itemstack = this.ingredientInv.getStackInSlot(i);
                if (itemstack.isEmpty())
                    refreshRecipe = true;
            }

            if (!remainingStack.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.ingredientInv.setInventorySlotContents(i, remainingStack);
                } else if (ItemStack.areItemsEqual(itemstack, remainingStack) && ItemStack.areItemStackTagsEqual(itemstack, remainingStack)) {
                    remainingStack.grow(itemstack.getCount());
                    this.ingredientInv.setInventorySlotContents(i, remainingStack);
                } else if (!player.inventory.addItemStackToInventory(remainingStack)) {
                    player.dropItem(remainingStack, false);
                }
            }
        }
        if (refreshRecipe)
            this.container.onCraftMatrixChanged(this.ingredientInv);
        return super.onTake(player, stack);
    }
}
