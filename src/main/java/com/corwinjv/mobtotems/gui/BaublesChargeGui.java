package com.corwinjv.mobtotems.gui;

import baubles.api.BaublesApi;
import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.items.baubles.BaubleItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

/**
 * Created by CorwinJV on 7/27/2016.
 */
public class BaublesChargeGui extends Gui
{
    private static final int ICON_BORDER = 2;
    private static final int ICON_WIDTH = 16;
    private static final int ICON_HEIGHT = 16;
    private static final int BG_BORDER = 1;
    private static final int BG_WIDTH = 18;
    private static final int BG_HEIGHT = 18;
    private static final int CHARGE_COLOR = 0xFF00FF00;

    private Minecraft minecraft = null;

    public BaublesChargeGui(Minecraft mc)
    {
        super();
        minecraft = mc;
    }

    @SubscribeEvent()
    public void onRenderOverlay(RenderGameOverlayEvent e)
    {
        if(e.isCancelable() || e.getType() != RenderGameOverlayEvent.ElementType.POTION_ICONS)
        {
            return;
        }

        IInventory baublesInventory = BaublesApi.getBaubles(minecraft.player);
        for(int i = 0; i < baublesInventory.getSizeInventory(); i++)
        {
            final ItemStack baubleStack = baublesInventory.getStackInSlot(i);

            if(baubleStack != null
                    && baubleStack.getItem() instanceof BaubleItem)
            {
                final BaubleItem baubleItem = (BaubleItem)baubleStack.getItem();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_LIGHTING);

                // Draw the background for the chargeable item
                minecraft.renderEngine.bindTexture(getGuiResourceLocation("chargeable_bg.png"));
                drawTexturedModalRect(BG_BORDER, (i * (ICON_HEIGHT + ICON_BORDER)) + BG_BORDER, 0, 0, BG_WIDTH, BG_HEIGHT);


                // TODO: Draw is currently activated

                // Draw the icon of the chargeable item
                minecraft.renderEngine.bindTexture(getGuiResourceLocation(baubleItem));
                drawTexturedModalRect(ICON_BORDER, (i * (ICON_HEIGHT + ICON_BORDER)) + ICON_BORDER, 0, 0, ICON_WIDTH, ICON_HEIGHT);

                // Draw the charge level beneath the icon
                int maxCharge = baubleItem.getMaxChargeLevel();
                int chargeLevel = baubleItem.getChargeLevel(baubleStack);
                float chargeRatio = (float)(chargeLevel) / maxCharge;

                int chargeWidth = (int)Math.floor(chargeRatio * ICON_WIDTH);
                int chargeTop = (i * (ICON_HEIGHT + ICON_BORDER)) + ICON_HEIGHT + ICON_BORDER;
                drawRect(ICON_BORDER, chargeTop, ICON_BORDER + chargeWidth, chargeTop + 2, CHARGE_COLOR);

                // TODO: (Maybe) Draw is on cooldown
            }
        }
    }

    public ResourceLocation getGuiResourceLocation(@Nonnull BaubleItem baubleItem)
    {
        String resourceName = baubleItem.getRegistryName().toString().substring(Reference.RESOURCE_PREFIX.length());
        return new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/" + resourceName + ".png");
    }

    public ResourceLocation getGuiResourceLocation(@Nonnull String guiFileName)
    {
        return new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/" + guiFileName);
    }

}
