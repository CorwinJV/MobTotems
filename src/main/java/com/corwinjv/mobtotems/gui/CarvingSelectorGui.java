package com.corwinjv.mobtotems.gui;

import amerifrance.guideapi.api.util.TextHelper;
import com.corwinjv.mobtotems.blocks.TotemType;
import javafx.scene.input.MouseButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static com.corwinjv.mobtotems.blocks.ModBlocks.TOTEM_WOOD;

/**
 * Created by CorwinJV on 1/19/2017.
 */
public class CarvingSelectorGui extends GuiScreen
{
    public CarvingSelectorGui() {
        super();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();

        int centerX = width / 2;
        int centerY = (height / 2) - 8;
        float angleOffset = 17;
        //float angleOffset = 0;
        GlStateManager.translate(0, 0, 0);

        Item item = Item.getItemFromBlock(TOTEM_WOOD);
        for(TotemType type : TotemType.values())
        {
            ItemStack stack = new ItemStack(item, 1, type.getMeta());
            float radius = 95;
            float baseAngle = (360 / TotemType.values().length);
            double angle =  (baseAngle * type.getMeta()) - angleOffset;

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
            int unselectedColor = 0xFFFFFFFF;
            int selectedColor = 0xFF00FFFF;

            GlStateManager.pushMatrix();
            GlStateManager.translate(x-25, y+13, 100F + this.zLevel);
            GlStateManager.scale(0.85, 0.85, 1.0);
            String text = TextHelper.localizeEffect(stack.getItem().getUnlocalizedName(stack) + ".name");

            // is text selected
            boolean isMouseOver = false;
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
                isMouseOver = true;
            }

            FMLLog.log(Level.ERROR, "angle: " + angle + " minToCheck: " + minToCheck + " maxToCheck" + maxToCheck + " mouseAngle: " + mouseAngle);


            fontRendererObj.drawStringWithShadow(text, 0, 0, (isMouseOver ? selectedColor : unselectedColor));
            fontRendererObj.drawStringWithShadow("", 0, 0, unselectedColor);
            GlStateManager.popMatrix();
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
        this.setupGuiTransform((int)x, (int)y, bakedmodel.isGui3d());
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        renderItem.renderItem(stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }

    private void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d)
    {
        GlStateManager.translate((float)xPosition, (float)yPosition, 100.0F + this.zLevel);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(32.0F, 32.0F, 32.0F);

        if (isGui3d)
        {
            GlStateManager.enableLighting();
        }
        else
        {
            GlStateManager.disableLighting();
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if(mouseButton == 1)
        {
            FMLClientHandler.instance().getClientPlayerEntity().closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
