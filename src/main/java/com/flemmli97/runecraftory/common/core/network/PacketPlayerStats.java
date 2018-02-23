package com.flemmli97.runecraftory.common.core.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPlayerStats  implements IMessage{

	public int str;	
	public int vit;
	public int intel;
	public PacketPlayerStats(){}
	
	/**0 = str, 1 = vit, 2 = intel (change later?)*/
	public PacketPlayerStats(IPlayer playerCap)
	{
		this.str = playerCap.getStr();
		this.vit = playerCap.getVit();
		this.intel = playerCap.getIntel();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		this.str = compound.getInteger("str");
		this.vit = compound.getInteger("vit");
		this.intel = compound.getInteger("intel");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("str", str);
		compound.setInteger("vit", vit);
		compound.setInteger("intel", intel);
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<PacketPlayerStats, IMessage> {

        @Override
        public IMessage onMessage(PacketPlayerStats msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(capSync != null)
		    {
				capSync.setIntel(player, msg.intel);
				capSync.setStr(player, msg.str);
				capSync.setVit(player, msg.vit);
		    }					
		}	
            return null;
        }
    }
}