package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.TotemWoodBlock;
import com.corwinjv.mobtotems.gui.CarvingSelectorGui;
import com.corwinjv.mobtotems.network.ActivateKnifeMessage;
import com.corwinjv.mobtotems.network.Network;
import com.corwinjv.mobtotems.network.OpenKnifeGuiMessage;
import com.corwinjv.mobtotems.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by CorwinJV on 1/14/2017.
 */
public class CarvingKnife extends ModItem {
    private final String COMPOUND_TAG = "carving_knife";
    private final String SELECTED_CARVING_TAG = "selected_carving";

    public CarvingKnife()
    {
        super();
    }

    @Override
    public void init() {
        super.init();
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if (world.isRemote)
        {
            Network.sendToServer(new ActivateKnifeMessage().setHand(hand));
            return new ActionResult(EnumActionResult.PASS, player.getHeldItem(hand));
        }
        return new ActionResult(EnumActionResult.FAIL, player.getHeldItem(hand));
    }

    @SideOnly(Side.CLIENT)
    public void openGui(EntityPlayer player, int meta)
    {
        FMLClientHandler.instance().displayGuiScreen(player, new CarvingSelectorGui(meta));
    }

    public void onKnifeActivated(EntityPlayer player, EnumHand hand)
    {
        if(player.world.isRemote)
        {
            return;
        }

        ItemStack stack = player.getHeldItem(hand);
        World world = player.world;
        Vec3d posEyes = player.getPositionEyes(1.0f);
        RayTraceResult rayTraceResult = world.rayTraceBlocks(posEyes, posEyes.add(player.getLookVec().scale(3f)));

        if(stack.getItem() instanceof CarvingKnife)
        {
            if(rayTraceResult != null)
            {
                BlockPos pos = rayTraceResult.getBlockPos();
                Block targetBlock = BlockUtils.getBlock(world, pos);

                if (targetBlock != null
                        && targetBlock instanceof TotemWoodBlock)
                {
                    world.setBlockState(pos, targetBlock.getDefaultState().withProperty(TotemWoodBlock.TOTEM_TYPE, TotemType.fromMeta(getSelectedCarving(stack))));
                }
            }
            else
            {
                Network.sendTo(new OpenKnifeGuiMessage().setMeta(getSelectedCarving(stack)), (EntityPlayerMP)player);
            }
        }
    }

    public void setSelectedCarving(ItemStack stack, int selectedCarving)
    {
        if(stack.getTagCompound() == null) {
            initNbtData(stack);
        }

        NBTTagCompound tagCompound = (NBTTagCompound)stack.getTagCompound().getTag(COMPOUND_TAG);
        tagCompound.setInteger(SELECTED_CARVING_TAG, selectedCarving);
        stack.getTagCompound().setTag(COMPOUND_TAG, tagCompound);
    }

    public int getSelectedCarving(ItemStack stack)
    {
        if(stack.getTagCompound() == null) {
            initNbtData(stack);
        }

        NBTTagCompound tagCompound = (NBTTagCompound)stack.getTagCompound().getTag(COMPOUND_TAG);
        return tagCompound.getInteger(SELECTED_CARVING_TAG);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        initNbtData(stack);
    }

    protected NBTTagCompound initNbtData(ItemStack stack)
    {
        if(stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger(SELECTED_CARVING_TAG, TotemType.NONE.getMeta());
        stack.getTagCompound().setTag(COMPOUND_TAG, tagCompound);
        return stack.getTagCompound();
    }
}
