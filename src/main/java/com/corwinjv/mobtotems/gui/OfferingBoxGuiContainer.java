package com.corwinjv.mobtotems.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

/**
 * Created by CorwinJV on 1/22/2017.
 */
public class OfferingBoxGuiContainer extends GuiContainer {

    public OfferingBoxGuiContainer(InventoryPlayer inventoryPlayer, IInventory offeringBoxTe) {
        super(new OfferingBoxContainer(inventoryPlayer, offeringBoxTe));
    }

    @Override
    public void initGui() {
        super.initGui();
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(util.getGuiResourceLocation("offering_box.png"));
        this.drawTexturedModalRect(this.getGuiLeft(), this.getGuiTop(), 0, 0, 176, 166);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
