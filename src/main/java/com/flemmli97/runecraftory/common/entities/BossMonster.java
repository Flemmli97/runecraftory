package com.flemmli97.runecraftory.common.entities;

import com.flemmli97.tenshilib.api.entity.IOverlayEntityRender;
import com.google.common.collect.Sets;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;

import java.util.Set;

public abstract class BossMonster extends BaseMonster implements IOverlayEntityRender {
    private final ServerBossInfo bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS);
    private static final DataParameter<Boolean> enraged = EntityDataManager.createKey(BossMonster.class, DataSerializers.BOOLEAN);

    public BossMonster(EntityType<? extends BossMonster> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(enraged, false);
    }

    public boolean isEnraged() {
        return this.isAlive() && !this.isTamed() && this.dataManager.get(enraged);
    }

    public void setEnraged(boolean flag, boolean load) {
        this.dataManager.set(enraged, flag);
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote) {
            if (!this.isTamed()) {
                this.updateplayers();
                this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
            }
        }
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (!this.isTamed() && this.getHealth() / this.getMaxHealth() < 0.5 && !this.isEnraged()) {
            this.setEnraged(true, false);

        }
    }

    @Override
    protected void tameEntity(PlayerEntity owner) {
        super.tameEntity(owner);
        this.setEnraged(false, false);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Enraged", this.isEnraged());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setEnraged(compound.getBoolean("Enraged"), true);
    }

    @Override
    public void setOwner(PlayerEntity player) {
        super.setOwner(player);
        if (player != null) {
            for (ServerPlayerEntity sPlayer : this.bossInfo.getPlayers()) {
                this.bossInfo.removePlayer(sPlayer);
            }
        }
    }

    private void updateplayers() {
        Set<ServerPlayerEntity> set = Sets.newHashSet();
        for (ServerPlayerEntity entityplayermp : this.world.getEntitiesWithinAABB(ServerPlayerEntity.class, this.getBoundingBox().grow(10.0))) {
            this.bossInfo.addPlayer(entityplayermp);
            set.add(entityplayermp);
        }
        Set<ServerPlayerEntity> set2 = Sets.newHashSet(this.bossInfo.getPlayers());
        set2.removeAll(set);
        for (ServerPlayerEntity entityplayermp2 : set2) {
            this.bossInfo.removePlayer(entityplayermp2);
        }
    }

    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void remove() {
        super.remove();
        for (ServerPlayerEntity entityplayermp1 : this.bossInfo.getPlayers()) {
            this.removeTrackingPlayer(entityplayermp1);
        }
    }

    @Override
    public int overlayU(int orig) {
        return this.isEnraged() ? (int) (Math.sin(this.ticksExisted / 7F) * 5 + 5) : orig;
    }

    @Override
    public int overlayV(int orig) {
        return this.isEnraged() ? 0 : orig;
    }
}
