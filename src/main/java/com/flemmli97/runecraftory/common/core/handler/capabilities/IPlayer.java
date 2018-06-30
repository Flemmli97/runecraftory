package com.flemmli97.runecraftory.common.core.handler.capabilities;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.core.handler.quests.QuestMission;
import com.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
import com.flemmli97.runecraftory.common.inventory.InventorySpells;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public interface IPlayer{
		
	public float getHealth();
	
	public void setHealth(EntityPlayer player, float amount);
	
	public void regenHealth(EntityPlayer player, float amount);
	
	public void damage(EntityPlayer player,DamageSource source, float amount);
	
	public float getMaxHealth();
	
	public void setMaxHealth(EntityPlayer player, float amount);
	
	public int getRunePoints();
	
	public int getMaxRunePoints();
	
	public boolean decreaseRunePoints(EntityPlayer player, int amount);
	
	public void refreshRunePoints(EntityPlayer player, int amount);

	public void setRunePoints(EntityPlayer player, int amount);
	
	public void setMaxRunePoints(EntityPlayer player, int amount);
			
	public int getMoney();
	
	public boolean useMoney(EntityPlayer player, int amount);

	public void setMoney(EntityPlayer player, int amount);

	public int[] getPlayerLevel();
	
	public void addXp(EntityPlayer player, int amount);
	
	public void setPlayerLevel(EntityPlayer player, int level, int xpAmount);
	
	//=====Player stats
	
	public float getStr();
	
	public void setStr(EntityPlayer player, float amount);
	
	public float getVit();
	
	public void setVit(EntityPlayer player, float amount);
	
	public float getIntel();
	
	public void setIntel(EntityPlayer player, float amount);
	
	//=====Skills
	
	public int[] getSkillLevel(EnumSkills skill);
	
	public void setSkillLevel(EnumSkills skill, EntityPlayer player, int level, int xp);
	
	public void increaseSkill(EnumSkills skill, EntityPlayer player, int xp);
	
	//=====NBT 
	
	public void readFromNBT(NBTTagCompound nbt);
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt, boolean forDeath);
	
	//=====Inventory
	
	public InventorySpells getInv();
	
	public InventoryShippingBin getShippingInv();
	
	//Quest
	@Nullable
	public QuestMission currentMission();
	
	public boolean acceptMission(QuestMission quest);
	
	public boolean finishMission(EntityPlayer player);
}
