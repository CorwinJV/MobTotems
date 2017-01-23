package com.corwinjv.mobtotems.gui;

import amerifrance.guideapi.api.util.TextHelper;
import com.corwinjv.mobtotems.Reference;
import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.network.Network;
import com.corwinjv.mobtotems.network.SetKnifeMetaMessage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static com.corwinjv.mobtotems.blocks.ModBlocks.TOTEM_WOOD;

/**
 * Created by CorwinJV on 1/19/2017.
 */
public class CarvingSelectorGui extends GuiScreen
{
    private static int INTRO_UPDATES = 5;
    private static int ANGLE_OFFSET = 17;

    private int updates = 0;
    private boolean in = true;

    private TotemType selectedType = TotemType.NONE;


    public CarvingSelectorGui(int meta) {
        super();
        selectedType = TotemType.fromMeta(meta);
    }

    public TotemType getTypeHoveredOver(float centerX, float centerY, int mouseX, int mouseY)
    {
        TotemType typeHoveredOver = TotemType.NONE;
        for(TotemType type : TotemType.values())
        {
            float baseAngle = (360 / TotemType.values().length);
            double angle =  (baseAngle * type.getMeta()) - ANGLE_OFFSET;

            double mouseAngle = getMouseAngle(new Vec2f(centerX, centerY), new Vec2f(mouseX, mouseY));
            double minToCheck = sanitizeDegree(angle - (baseAngle / 2));
            double maxToCheck = sanitizeDegree(angle + (baseAngle / 2));
            if(minToCheck > maxToCheck)
            {
                minToCheck -= 360;
            }
            if(mouseAngle > minToCheck
                    && mouseAngle < maxToCheck)
            {
                typeHoveredOver = type;
            }
        }
        return typeHoveredOver;
    }

    public TotemType getTypeSelected()
    {
        return selectedType;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();

        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableTexture2D();

        int centerX = width / 2;
        int centerY = (height / 2) - 8;

        TotemType typeHovered = getTypeHoveredOver(centerX, centerY, mouseX, mouseY);
        TotemType typeSelected = getTypeSelected();

        Item item = Item.getItemFromBlock(TOTEM_WOOD);
        for(TotemType type : TotemType.values())
        {
            ItemStack stack = new ItemStack(item, 1, type.getMeta());
            float radius = Math.min(updates * 20, 95f);
            float baseAngle = (360 / TotemType.values().length);
            double angle =  (baseAngle * type.getMeta()) - ANGLE_OFFSET;

            angle = sanitizeDegree(angle);

            double x = centerX + Math.cos(Math.toRadians(angle)) * radius;
            double y = centerY + Math.sin(Math.toRadians(angle)) * radius;

            // Render bg
            mc.renderEngine.bindTexture(util.getGuiResourceLocation("chargeable_bg.png"));
            GlStateManager.pushMatrix();
            GlStateManager.translate(x-18.5, y-18.5, 90.0F + this.zLevel);
            GlStateManager.scale(2.0f, 2.0f, 1.0f);
            drawTexturedModalRect(0, 0, 0, 0, 32, 32);
            GlStateManager.popMatrix();

            // Render item
            IBakedModel model = mc.getRenderItem().getItemModelWithOverrides(stack, null, null);
            renderItemModel(stack, x, y, model, mc.getRenderItem());

            // Render Text
            int unselectedColor = 0xFFFFFF;
            int selectedColor = 0xFFAA00;
            int hoveredColor = 0x55FFFF;

            GlStateManager.pushMatrix();
            GlStateManager.translate(x-25, y+13, 100F + this.zLevel);
            GlStateManager.scale(0.85, 0.85, 1.0);
            String text = TextHelper.localizeEffect(stack.getItem().getUnlocalizedName(stack) + ".name");

            // is text selected
            int colorToPrint = unselectedColor;
            if(type == typeSelected)
            {
                colorToPrint = selectedColor;
            }
            if(type == typeHovered)
            {
                colorToPrint = hoveredColor;
            }

            fontRendererObj.drawStringWithShadow(text, 0, 0, colorToPrint);
            fontRendererObj.drawStringWithShadow("", 0, 0, unselectedColor);
            GlStateManager.popMatrix();
        }

        if(updates >= INTRO_UPDATES)
        {
            fontRendererObj.drawStringWithShadow(TextHelper.localizeEffect(Reference.RESOURCE_PREFIX + "gui.carving.leftclick"), centerX - 50, centerY - 5, 0xFFFFFF);
            fontRendererObj.drawStringWithShadow(TextHelper.localizeEffect(Reference.RESOURCE_PREFIX + "gui.carving.rightclick"), centerX - 50, 15 + centerY, 0xFFFFFF);
        }

        GlStateManager.popMatrix();
    }

    public double getMouseAngle(Vec2f referencePoint, Vec2f mousePos)
    {
        Vec2f relativePos = new Vec2f(mousePos.x - referencePoint.x, mousePos.y - referencePoint.y);
        double degrees = Math.toDegrees(Math.atan2(relativePos.y, relativePos.x));
        return sanitizeDegree(degrees);
    }

    public double sanitizeDegree(double degree)
    {
        if(degree < 0)
        {
            degree += 360;
        }
        if(degree > 360)
        {
            degree -= 360;
        }
        return degree;
    }

    protected void renderItemModel(ItemStack stack, double x, double y, IBakedModel bakedmodel, RenderItem renderItem)
    {
        GlStateManager.pushMatrix();
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.setupGuiTransform((int)x, (int)y);
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        renderItem.renderItem(stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }

    private void setupGuiTransform(int xPosition, int yPosition)
    {
        GlStateManager.translate((float)xPosition, (float)yPosition, 100.0F + this.zLevel);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(32.0F, 32.0F, 32.0F);

        GlStateManager.enableLighting();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if(in)
        {
            updates++;
            if(updates > INTRO_UPDATES)
            {
                updates = INTRO_UPDATES;
            }
        }
        else
        {
            updates--;
            if(updates < 0)
            {
                updates = 0;
                mc.player.closeScreen();
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if(mouseButton == 0)
        {
            int centerX = width / 2;
            int centerY = (height / 2) - 8;

            selectedType = getTypeHoveredOver(centerX, centerY, mouseX, mouseY);
            Network.sendToServer(new SetKnifeMetaMessage(selectedType.getMeta(), EnumHand.MAIN_HAND));
        }

        if(mouseButton == 0 || mouseButton == 1)
        {
            in = false;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
