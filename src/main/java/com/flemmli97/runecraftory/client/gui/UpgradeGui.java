package com.flemmli97.runecraftory.client.gui;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.inventory.container.ContainerUpgrade;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class UpgradeGui extends ContainerScreen<ContainerUpgrade> {

    private static final ResourceLocation forging = new ResourceLocation(RuneCraftory.MODID, "textures/gui/forgef.png");
    private static final ResourceLocation crafting = new ResourceLocation(RuneCraftory.MODID, "textures/gui/craftingf.png");

    public UpgradeGui(ContainerUpgrade container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    protected void drawBackground(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        /*switch (this.container.craftingType()) {
            case ARMOR:
                this.client.getTextureManager().bindTexture(crafting);
                this.drawTexture(stack, this.guiLeft, this.guiTop, 0, 0, 176, 166);
                break;
            case FORGE:
                this.client.getTextureManager().bindTexture(forging);
                this.drawTexture(stack, this.guiLeft, this.guiTop, 0, 0, 176, 166);
                break;
        }*/
        this.client.getTextureManager().bindTexture(forging);
        this.drawTexture(stack, this.guiLeft, this.guiTop, 0, 0, 176, 166);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(stack, mouseX, mouseY);
    }
}
