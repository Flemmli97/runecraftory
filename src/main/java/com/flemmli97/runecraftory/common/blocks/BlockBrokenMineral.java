package com.flemmli97.runecraftory.common.blocks;

import java.util.Random;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.BlockMineral.EnumTier;
import com.flemmli97.runecraftory.common.blocks.tile.TileBrokenOre;
import com.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import com.flemmli97.runecraftory.common.items.weapons.HammerBase;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBrokenMineral extends Block implements ITileEntityProvider{
	
    public static final PropertyEnum<BlockMineral.EnumTier> TIER = PropertyEnum.<BlockMineral.EnumTier>create("tier", BlockMineral.EnumTier.class);

	public BlockBrokenMineral() {
		super(Material.ROCK);
		this.setCreativeTab(RuneCraftory.blocks);
        this.blockSoundType = SoundType.STONE;
        this.setResistance(5.0F);
        this.setHardness(3.0F);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "broken_ore"));
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.setTickRandomly(true);
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		state.withProperty(TIER, EnumTier.fromMeta(stack.getMetadata()));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TIER});
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		if(player.capabilities.isCreativeMode)
	        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), world.isRemote ? 11 : 3);
		else if((player.getHeldItemMainhand().getItem() instanceof ItemToolHammer || player.getHeldItemMainhand().getItem() instanceof HammerBase) && player.isSneaking())
		{
			return world.setBlockState(pos, Blocks.AIR.getDefaultState(), world.isRemote ? 11 : 3);
		}
		return false;
	}
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TIER, BlockMineral.EnumTier.fromMeta(meta));
    }
	
	@Override
    public int getMetaFromState(IBlockState state)
    {
    		return state.getValue(TIER).getMeta();
    }
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab==RuneCraftory.blocks)
			for(EnumTier tier : EnumTier.values())
			{
				items.add(new ItemStack(this, 1, tier.getMeta()));
			}
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(state);
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileBrokenOre(world);
	}
}