package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIMeleeBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.world.World;

public class EntityAnt extends EntityMobBase {

    public EntityAIMeleeBase<EntityAnt> attack = new EntityAIMeleeBase<EntityAnt>(this, 1.0f);
    private static final AnimatedAction melee = new AnimatedAction(25,20, "attack");

    private static final AnimatedAction[] anims = new AnimatedAction[] {melee};

    public EntityAnt(World world) {
        super(world);
        this.setSize(0.6f, 0.45f);
        this.tasks.addTask(2, this.attack);
    }

    @Override
    public float attackChance() {
        return 0.6f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return type==AnimationType.MELEE?anim.getID().equals("attack"):true;
    }
}
