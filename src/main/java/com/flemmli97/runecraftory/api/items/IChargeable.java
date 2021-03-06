package com.flemmli97.runecraftory.api.items;

import com.flemmli97.runecraftory.api.enums.EnumToolCharge;
import net.minecraft.item.ItemStack;

public interface IChargeable {

    int getChargeTime(ItemStack stack);

    int chargeAmount(ItemStack stack);

    EnumToolCharge chargeType(ItemStack stack);
}
