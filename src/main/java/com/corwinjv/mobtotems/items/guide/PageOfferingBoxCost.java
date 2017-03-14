package com.corwinjv.mobtotems.items.guide;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.gui.GuiBase;
import amerifrance.guideapi.gui.GuiEntry;
import com.corwinjv.mobtotems.MobTotems;
import com.corwinjv.mobtotems.TotemHelper;
import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.gui.util;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 2/6/2017.
 */
public class PageOfferingBoxCost implements IPage {
    public static final int ICON_WIDTH = 18;
    TotemType totemType = null;
    List<ItemStack> itemStacks = new ArrayList<>();

    public PageOfferingBoxCost(TotemType totemType) {
        this.totemType = totemType;
        itemStacks = TotemHelper.getCostForTotemType(totemType);
    }

    @Override
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRendererObj) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        MobTotems.component().textureManager().bindTexture(util.getGuiResourceLocation("offering_box_cost_gui.png"));
        guiBase.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 166);

        fontRendererObj.drawString(util.getLocalizedGuideText("cost_label"), guiLeft + 85, guiTop + 85, 0);

        for (int xSlot = 0; xSlot < 3; xSlot++) {
            for (int ySlot = 0; ySlot < 3; ySlot++) {
                int index = ySlot * 3 + xSlot;
                if (index < itemStacks.size()) {
                    // Render item stack in grid
                    int left = (guiLeft + 70) + xSlot * ICON_WIDTH;
                    int top = (guiTop + 20) + ySlot * ICON_WIDTH;
                    ItemStack stack = itemStacks.get(ySlot * 3 + xSlot);
                    IBakedModel model = guiBase.mc.getRenderItem().getItemModelWithOverrides(stack, null, null);
                    GuiHelper.drawItemStack(stack, left, top);

                    // Check if mouse is over this stack
                    //FMLLog.log(Level.ERROR, "mouseX: " + mouseX + "mouseY: " + mouseY + "top: " + top + "left")
                    if (GuiHelper.isMouseBetween(mouseX, mouseY, left, top, ICON_WIDTH, ICON_WIDTH)) {
                        // render tooltip
                        List<String> tooltip = GuiHelper.getTooltip(stack);
                        if (tooltip != null) {
                            guiBase.drawHoveringText(tooltip, mouseX, mouseY);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void drawExtras(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRendererObj) {

    }

    @Override
    public boolean canSee(Book book, CategoryAbstract category, EntryAbstract entry, EntityPlayer player, ItemStack bookStack, GuiEntry guiEntry) {
        return true;
    }

    @Override
    public void onLeftClicked(Book book, CategoryAbstract category, EntryAbstract entry, int mouseX, int mouseY, EntityPlayer player, GuiEntry guiEntry) {
    }

    @Override
    public void onRightClicked(Book book, CategoryAbstract category, EntryAbstract entry, int mouseX, int mouseY, EntityPlayer player, GuiEntry guiEntry) {
    }

    @Override
    public void onInit(Book book, CategoryAbstract category, EntryAbstract entry, EntityPlayer player, ItemStack bookStack, GuiEntry guiEntry) {
    }
}
