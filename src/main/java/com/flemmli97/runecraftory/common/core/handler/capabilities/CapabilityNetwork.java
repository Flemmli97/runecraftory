package com.flemmli97.runecraftory.common.core.handler.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityNetwork
{
    public static class PlayerCapNetwork implements Capability.IStorage<IPlayer>
    {
        public NBTBase writeNBT(Capability<IPlayer> capability, IPlayer instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            instance.writeToNBT(compound, false);
            return compound;
        }
        
        public void readNBT(Capability<IPlayer> capability, IPlayer instance, EnumFacing side, NBTBase nbt) {
            instance.readFromNBT((NBTTagCompound)nbt);
        }
    }
    
    public static class PlayerAnimNetwork implements Capability.IStorage<IPlayerAnim>
    {
        public NBTBase writeNBT(Capability<IPlayerAnim> capability, IPlayerAnim instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            return compound;
        }
        
        public void readNBT(Capability<IPlayerAnim> capability, IPlayerAnim instance, EnumFacing side, NBTBase nbt) {
        }
    }
}