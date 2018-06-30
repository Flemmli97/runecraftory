package com.flemmli97.runecraftory;

import com.flemmli97.runecraftory.common.commands.CommandStructure;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = LibReference.MODID, name = LibReference.MODNAME, version = LibReference.VERSION)
public class RuneCraftory
{
    @Instance
    public static RuneCraftory instance = new RuneCraftory();
    
    @SidedProxy(clientSide = LibReference.clientProxy, serverSide = LibReference.commonProxy)
    public static CommonProxy proxy;
    
    @EventHandler
    public void construction(FMLConstructionEvent event) {
        FluidRegistry.enableUniversalBucket();
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandStructure());
    }
    
    public static CreativeTabs weaponToolTab = new CreativeTabs("runecraftory.weaponsTools") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.icon, 1, 0);
        }
    };
    
    public static CreativeTabs equipment = new CreativeTabs("runecraftory.equipment") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.cheapBracelet);
        }
    };
    
    public static CreativeTabs upgradeItems = new CreativeTabs("runecraftory.upgrade") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.mineral);
        }
    };
    
    public static CreativeTabs blocks = new CreativeTabs("runecraftory.blocks") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.itemBlockForge);
        }
    };
    
    public static CreativeTabs cast = new CreativeTabs("runecraftory.cast") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.icon, 1, 1);
        }
    };
    
    public static CreativeTabs crops = new CreativeTabs("runecraftory.crops") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.turnipSeeds);
        }
    };
    
    public static CreativeTabs monsters = new CreativeTabs("runecraftory.monsters") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.icon, 1, 2);
        }
    };
    
}
