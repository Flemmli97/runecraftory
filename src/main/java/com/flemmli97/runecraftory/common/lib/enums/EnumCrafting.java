package com.flemmli97.runecraftory.common.lib.enums;

public enum EnumCrafting {
	
	FORGE(0),
	ARMOR(1),
	PHARMA(2),
	COOKING(3);
	
	private int id;
	EnumCrafting(int id)
	{
		this.id=id;
	}

	public int getID()
	{
		return this.id;
	}
}
