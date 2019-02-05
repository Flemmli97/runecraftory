package com.flemmli97.runecraftory.common.blocks.crops;

import java.util.Random;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.api.items.CropProperties;
import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.common.blocks.tile.TileCrop;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.utils.ItemNBT;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockCropBase extends BlockBush implements IGrowable, ITileEntityProvider{
	
    private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    public static final PropertyInteger STATUS = PropertyInteger.create("status", 0, 3);

    private String crop;
	public BlockCropBase(String name, String oreDictName)
	{
		super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(STATUS, 0));
		this.setTickRandomly(false);
		this.crop=oreDictName;
		this.setSoundType(SoundType.PLANT);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "plant_"+name));
        this.setUnlocalizedName(this.getRegistryName().toString());
        CropMap.addPlant(oreDictName, this);
	}
    
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {STATUS});
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
		TileEntity tile = world.getTileEntity(pos);
		int age = 0;
		if(tile instanceof TileCrop)
			age=(int) (((TileCrop)tile).age()*3 / (float)this.properties().growth());
        return state.withProperty(BlockCropBase.STATUS, MathHelper.clamp(age, 0, 3));
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }
	
	@Override
    public int getMetaFromState(IBlockState state)
    {
    	return 0;
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		TileCrop tile = (TileCrop) world.getTileEntity(pos);
		if(tile.age()>=this.properties().growth() && player.getHeldItem(hand).isEmpty())
		{
			this.dropBlockAsItem(world, pos, state, 0);
			if(this.properties().regrowable())
			{
				tile.resetAge();
				world.setBlockState(pos, state.withProperty(STATUS, 0));
			}
			else
				world.setBlockToAir(pos);
			return true;
		}
		return false;
    }
	
	@Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
    {
		return EnumPlantType.Crop;
    }
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		
        return CROPS_AABB[this.getActualState(state, source, pos).getValue(STATUS)];
    }
	
	public CropProperties properties()
	{
		return CropMap.getProperties(new ItemStack(CropMap.seedFromString(this.crop)), true);
	}

	@Override
    protected boolean canSustainBush(IBlockState state)
    {
        return state.getBlock() == Blocks.FARMLAND;
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        IBlockState soil = worldIn.getBlockState(pos.down());
        return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
    	TileCrop tile = (TileCrop) world.getTileEntity(pos);
    	int age = tile.age();
        if (age >= this.properties().growth())
        {          
        	ItemStack stack = tile.isGiant()?new ItemStack(CropMap.giantCropFromString(this.crop), this.properties().maxDrops()):new ItemStack(CropMap.cropFromString(this.crop), this.properties().maxDrops());
            tile.postProcess();
        	drops.add(ItemNBT.getLeveledItem(stack, tile.level()));
        }
    }
    
    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (willHarvest) return true;
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool)
    {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }

    /**
     * Whether this IGrowable can grow
     */
    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
    {
        return !((TileCrop)world.getTileEntity(pos)).isFullyGrown(this);
    }
    
    /**
     * Use as a soil fertilizer?
     */
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }
    /**
     * Used for bonemeal items.
     */
    @Override
    public void grow(World world, Random rand, BlockPos pos, IBlockState state)
    {
        
    }
    
	@Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state)
    {
        return ItemNBT.getLeveledItem(new ItemStack(CropMap.cropFromString(this.crop), 1), 1);
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCrop();
	}
}
