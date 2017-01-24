package com.corwinjv.mobtotems.gui;

import amerifrance.guideapi.api.util.TextHelper;
import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.tiles.TotemTileEntity;
import com.corwinjv.mobtotems.interfaces.IChargeableTileEntity;
import com.corwinjv.mobtotems.interfaces.IMultiblock;
import com.sun.org.apache.bcel.internal.generic.IMUL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.List;

/**
 * Created by CorwinJV on 1/22/2017.
 */
public class OfferingBoxGuiContainer extends GuiContainer {

    private IChargeableTileEntity chargeableTileEntity = null;
    private IMultiblock<TotemType> multiblockTileEntity = null;
    private BlockPos multiBlockTileEntityPos = null;

    public OfferingBoxGuiContainer(InventoryPlayer inventoryPlayer, IInventory inventory) {
        super(new OfferingBoxContainer(inventoryPlayer, inventory));
        if(inventory instanceof IChargeableTileEntity)
        {
            chargeableTileEntity = (IChargeableTileEntity)inventory;
        }
        if(inventory instanceof IMultiblock)
        {
            this.multiblockTileEntity = (IMultiblock)inventory;
        }
        if(inventory instanceof TileEntity)
        {
            this.multiBlockTileEntityPos = ((TileEntity) inventory).getPos();
        }
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
        this.mc.getTextureManager().bindTexture(util.getGuiResourceLocation("offering_box_gui.png"));
        this.drawTexturedModalRect(this.getGuiLeft(), this.getGuiTop(), 0, 0, 176, 166);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        // Display charge level
        if(chargeableTileEntity != null)
        {
            int chargeLevel = chargeableTileEntity.getChargeLevel();
            float chargeRatio = (float)(chargeLevel) / chargeableTileEntity.getMaxChargeLevel();

            //FMLLog.log(Level.ERROR, "chargeLevel: " + chargeLevel + " chargeRatio: " + chargeRatio);

            int chargeBottom = 65;
            int chargeLeft = 138;
            int chargeWidth = 8;

            // Bar
            drawRect(chargeLeft, 15, chargeLeft + chargeWidth, chargeBottom, Reference.CHARGE_COLOR);

            // Charge text
            GlStateManager.pushMatrix();
            GlStateManager.translate(chargeLeft - 14, chargeBottom + 2, 100F + this.zLevel);
            GlStateManager.scale(0.6, 0.6, 1.0);
            String text = "Charge level";
            fontRendererObj.drawStringWithShadow(text, 0, 0, 0xffffff);
            GlStateManager.popMatrix();
        }

        // Display totem composition
        TileEntity te = Minecraft.getMinecraft().world.getTileEntity(multiBlockTileEntityPos);
        if(te instanceof IMultiblock)
        {
            if(((IMultiblock)te).getIsMaster())
            {
                int totemTextTop = 20;
                String text = "Totem Parts: ";
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.7, 0.7, 1.0);
                fontRendererObj.drawStringWithShadow(text, 0, totemTextTop, 0xffffff);

                StringBuilder stringBuilder = new StringBuilder();
                try
                {
                    List<BlockPos> slaves = ((IMultiblock)te).getSlaves();
                    int lines = 0;

                    // I know this is super hacky and it's because I'm meddling with forces I do not comprehend, and I'm okay with that.
                    for(BlockPos slavePos : slaves)
                    {
                        TileEntity slaveTe = Minecraft.getMinecraft().world.getTileEntity(slavePos);
                        if(slaveTe instanceof TotemTileEntity)
                        {
                            String text2 = "";
                            if(((TotemTileEntity) slaveTe).getType() == TotemType.NONE)
                            {
                                text2 = TextHelper.localizeEffect("tiles.mobtotems:totem_wood." + ((TotemTileEntity) slaveTe).getType().getName() + ".shortname");
                            }
                            else
                            {
                                text2 = TextHelper.localizeEffect("tiles.mobtotems:totem_wood." + ((TotemTileEntity) slaveTe).getType().getName() + ".name");
                            }
                            fontRendererObj.drawStringWithShadow(text2, 0, ((lines + 1)* 10) + totemTextTop, 0xffffff);
                            lines++;
                        }
                    }
                }
                catch(Exception e) {
                    FMLLog.log(Level.ERROR, "Offering box - Unknown slave types.");
                    e.printStackTrace();
                }

                String text2 = stringBuilder.toString();
                GlStateManager.popMatrix();
            }

        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
