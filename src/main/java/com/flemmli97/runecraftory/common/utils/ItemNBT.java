package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.TreeMap;

public class ItemNBT {

    public static int itemLevel(ItemStack stack) {
        CompoundNBT tag = getItemNBT(stack);
        return tag != null ? tag.getInt("ItemLevel") : 1;
    }

    public static boolean addItemLevel(ItemStack stack) {
        if (itemLevel(stack) < 10) {
            CompoundNBT tag = getItemNBT(stack);
            if (tag != null) {
                tag.putInt("ItemLevel", MathHelper.clamp(tag.getInt("ItemLevel") + 1, 1, 10));
                return true;
            }
        }
        return false;
    }

    public static ItemStack getLeveledItem(ItemStack stack, int level) {
        CompoundNBT compound = ItemNBT.getItemNBT(stack);
        if (compound != null) {
            compound.putInt("ItemLevel", MathHelper.clamp(level, 1, 10));
        }
        return stack;
    }

    public static Map<Attribute, Integer> statIncrease(ItemStack stack) {
        Map<Attribute, Integer> map = new TreeMap<>(ModAttributes.sorted);
        if (stack.getItem() instanceof IItemUsable) {
            CompoundNBT compound = getItemNBT(stack);
            if (compound != null) {
                CompoundNBT tag = compound.getCompound("ItemStats");
                for (String attName : tag.keySet()) {
                    Attribute att = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attName));
                    if (att.getRegistryName().toString().equals(attName))
                        map.put(att, tag.getInt(attName));
                }
            }
        } else {
            ItemStat stat = DataPackHandler.getStats(stack.getItem());
            if (stat != null) {
                return stat.itemStats();
            }
        }
        return map;
    }

    @Nullable
    public static CompoundNBT getItemNBT(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(RuneCraftory.MODID)) {
            return stack.getTag().getCompound(RuneCraftory.MODID);
        }
        if (initNBT(stack)) {
            return stack.getTag().getCompound(RuneCraftory.MODID);
        }
        return null;
    }

    public static boolean initNBT(ItemStack stack) {
        return initNBT(stack, false);
    }

    public static boolean initNBT(ItemStack stack, boolean forced) {
        ItemStat stat = DataPackHandler.getStats(stack.getItem());
        if (stat != null || forced) {
            CompoundNBT stackTag = stack.getOrCreateTag();
            CompoundNBT compound = new CompoundNBT();
            compound.putInt("ItemLevel", 1);
            if (stack.getItem() instanceof IItemWearable) {
                compound.put("Upgrades", new ListNBT());
                if (stat != null) {
                    CompoundNBT stats = new CompoundNBT();
                    for (Map.Entry<Attribute, Integer> entry : stat.itemStats().entrySet()) {
                        stats.putInt(entry.getKey().getRegistryName().toString(), entry.getValue());
                    }
                    compound.put("ItemStats", stats);
                    if (stack.getItem() instanceof IItemUsable) {
                        compound.putString("Element", stat.element().getTranslation());
                    }
                }
            }
            stackTag.put(RuneCraftory.MODID, compound);
            stack.setTag(stackTag);
            return true;
        }
        return false;
    }
}
