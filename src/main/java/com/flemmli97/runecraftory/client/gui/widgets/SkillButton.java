package com.flemmli97.runecraftory.client.gui.widgets;

import com.flemmli97.runecraftory.RuneCraftory;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class SkillButton extends Button {

    private static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");

    public SkillButton(int x, int y, IPressable press) {
        super(x, y, 24, 24, StringTextComponent.EMPTY, press);
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(texturepath);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawTexture(stack, this.x, this.y, 98 + (this.hovered ? 25 : 0), 14, this.width, this.height);
    }
}
