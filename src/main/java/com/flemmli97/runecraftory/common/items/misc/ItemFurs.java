package com.flemmli97.runecraftory.common.items.misc;

import java.util.List;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IItemBase;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFurs extends Item implements IItemBase{
	
	private String[] bones = new String[] {"woolS", "woolM", "woolL","fur","furball","yellow", "quality", "puffy", "penguin", "mane", "red", "blue","chest"};

	public ItemFurs()
	{
		super();
		this.setCreativeTab(RuneCraftory.upgradeItems);
        	this.setRegistryName(new ResourceLocation(LibReference.MODID, "furs" ));	
        	this.setUnlocalizedName(this.getRegistryName().toString());
        	this.setHasSubtypes(true);
        	this.initStats();
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
	}

	public void initStats()
	{
		
	}
	
	@Override
	public String getUnlocalizedName() {
		return this.getRegistryName().toString();
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_"+ this.bones[stack.getMetadata()];
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab==RuneCraftory.upgradeItems)
		for (int i = 0; i < 13; i ++) {
			items.add(new ItemStack(this, 1, i));
	    }
	}
	@Override
	public int getBuyPrice(ItemStack stack) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getSellPrice(ItemStack stack) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getUpgradeDifficulty() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public NBTTagCompound defaultNBTStats(ItemStack stack)
	{	
		return null;
	}


	@SideOnly(Side.CLIENT)
	public void initModel() {
		for(int meta = 0; meta < 12;meta++)
			ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(getRegistryName()+":"+meta, "inventory"));		
	}

}