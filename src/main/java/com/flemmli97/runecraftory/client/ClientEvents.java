package com.flemmli97.runecraftory.client;

import com.flemmli97.runecraftory.api.datapack.CropProperties;
import com.flemmli97.runecraftory.api.datapack.FoodProperties;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.flemmli97.runecraftory.client.gui.widgets.SkillButton;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.items.consumables.ItemMedicine;
import com.flemmli97.runecraftory.common.network.C2SOpenInfo;
import com.flemmli97.runecraftory.common.network.C2SRideJump;
import com.flemmli97.runecraftory.common.network.C2SSpellKey;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ClientEvents {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }

    @SubscribeEvent
    public void jump(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof ClientPlayerEntity && e.getEntityLiving().getRidingEntity() instanceof BaseMonster && ((ClientPlayerEntity) e.getEntityLiving()).movementInput.jump)
            PacketHandler.sendToServer(new C2SRideJump());
    }

    @SubscribeEvent(receiveCanceled = true)
    public void keyEvent(InputEvent.KeyInputEvent event) {
        if (ClientHandlers.spell1.isPressed()) {
            PacketHandler.sendToServer(new C2SSpellKey(0));
        }
        if (ClientHandlers.spell2.isPressed()) {
            PacketHandler.sendToServer(new C2SSpellKey(1));
        }
        if (ClientHandlers.spell3.isPressed()) {
            PacketHandler.sendToServer(new C2SSpellKey(2));
        }
        if (ClientHandlers.spell4.isPressed()) {
            PacketHandler.sendToServer(new C2SSpellKey(3));
        }
    }

    @SubscribeEvent
    public void renderRunePoints(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD || event.getType() == RenderGameOverlayEvent.ElementType.HEALTH)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void initSkillTab(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof InventoryScreen || event.getGui() instanceof CreativeScreen) {
            int x = 0;
            int y = 0;
            if (event.getGui() instanceof InventoryScreen) {
                x = (event.getGui().width - 174) / 2 - 28;
                y = (event.getGui().height - 166) / 2;
            } else {
                x = (event.getGui().width - 192) / 2 - 28;
                y = (event.getGui().height - 136) / 2;
            }
            event.addWidget(new SkillButton(x, y, b -> PacketHandler.sendToServer(new C2SOpenInfo(C2SOpenInfo.Type.MAIN))));
        }
    }

    @SubscribeEvent
    public void renderRunePoints(RenderGameOverlayEvent.Post event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE)
            return;
        if (ClientHandlers.overlay != null)
            ClientHandlers.overlay.renderBar(event.getMatrixStack());
        if (ClientHandlers.spellDisplay != null)
            ClientHandlers.spellDisplay.render(event.getMatrixStack(), event.getPartialTicks());
    }

    @SubscribeEvent
    public void tooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty()) {
            boolean showTooltip = true;
            if (stack.hasTag() && stack.getTag().contains("HideFlags", 99)) {
                showTooltip = (stack.getTag().getInt("HideFlags") & 0x20) == 0x0;
            }
            if (showTooltip) {
                event.getToolTip().addAll(1, this.injectAdditionalTooltip(stack));
            }
        }
    }

    private List<ITextComponent> injectAdditionalTooltip(ItemStack stack) {
        List<ITextComponent> tooltip = Lists.newArrayList();
        boolean shift = Screen.hasShiftDown();
        ItemStat stat = DataPackHandler.getStats(stack.getItem());
        if (stat != null) {
            tooltip.addAll(stat.texts(stack, shift));
        }
        CropProperties props = DataPackHandler.getCropStat(stack.getItem());
        if (props != null) {
            tooltip.addAll(props.texts());
        }
        if (shift) {
            FoodProperties food = DataPackHandler.getFoodStat(stack.getItem());
            if (food != null) {
                if (stack.getItem() instanceof ItemMedicine)
                    tooltip.addAll(food.medicineText((ItemMedicine) stack.getItem(), stack));
                else
                    tooltip.addAll(food.texts());
            }
        }
        return tooltip;
    }

    @SubscribeEvent
    public void worldRender(RenderWorldLastEvent event) {
        /*if(WeatherData.get(Minecraft.getMinecraft().world).currentWeather()==EnumWeather.RUNEY)
            this.renderRuneyWeather(Minecraft.getMinecraft(), event.getPartialTicks());
        *///if(ConfigHandler.MainConfig.debugAttack)
        //AttackAABBRender.INST.render(event.getMatrixStack(), event.getContext().getEntityFramebuffer());
    }
}
