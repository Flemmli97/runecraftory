package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemSkillUp extends Item {

    public ItemSkillUp(Properties props) {
        super(props);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                for (EnumSkills skill : EnumSkills.values())
                    cap.increaseSkill(skill, (ServerPlayerEntity) player, LevelCalc.xpAmountForSkills(cap.getSkillLevel(skill)[0]) / 2);
            });
        }
        return ActionResult.success(player.getHeldItem(hand));
    }
}