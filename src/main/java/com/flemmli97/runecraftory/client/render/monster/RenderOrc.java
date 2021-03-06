package com.flemmli97.runecraftory.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.model.monster.ModelOrc;
import com.flemmli97.runecraftory.client.render.RenderMonster;
import com.flemmli97.runecraftory.common.entities.monster.EntityOrc;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderOrc<T extends EntityOrc> extends RenderMonster<T, ModelOrc<T>> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/orc.png");

    public RenderOrc(EntityRendererManager renderManager) {
        super(renderManager, new ModelOrc<>());
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }

}
