package com.flemmli97.runecraftory.common.spells;

import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import com.flemmli97.runecraftory.common.entities.projectiles.EntityFireball;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;

public class FireballSpell extends Spell {

    @Override
    public void update(PlayerEntity player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayerEntity player) {
        player.getCapability(PlayerCapProvider.PlayerCap).ifPresent(cap-> LevelCalc.levelSkill(player, cap, EnumSkills.FIRE, 1));
    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerWorld world, LivingEntity entity, ItemStack stack) {
        boolean rp = !(entity instanceof PlayerEntity) || entity.getCapability(PlayerCapProvider.PlayerCap).map(cap->cap.decreaseRunePoints((PlayerEntity) entity, this.rpCost(), true)).orElse(false);
        if(rp){
            EntityFireball ball = new EntityFireball(world, entity);
            ball.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0, 1, 0);
            world.addEntity(ball);
            return true;
        }
        return false;
    }

    @Override
    public int rpCost() {
        return 10;
    }
}
