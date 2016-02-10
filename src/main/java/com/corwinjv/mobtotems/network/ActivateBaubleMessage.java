package com.corwinjv.mobtotems.network;

import baubles.api.BaublesApi;
import com.corwinjv.mobtotems.items.BaubleItem;
import com.corwinjv.mobtotems.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * Created by CorwinJV on 2/1/2016.
 */
public class ActivateBaubleMessage extends Message<ActivateBaubleMessage>
{
    @Override
    protected void handleServer(ActivateBaubleMessage message, EntityPlayerMP player)
    {
        final IInventory baubleInventory = BaublesApi.getBaubles(player);

        if(baubleInventory == null)
        {
            return;
        }

        for(int i = 0; i < baubleInventory.getSizeInventory(); i++)
        {
            final ItemStack baubleStack = baubleInventory.getStackInSlot(i);
            if(baubleStack != null
                    && baubleStack.getItem() instanceof BaubleItem)
            {
                final EntityPlayerMP fPlayer = player;
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        ((BaubleItem) baubleStack.getItem()).onBaubleActivated(baubleStack, fPlayer);
                    }
                });
            }
        }
    }
}
