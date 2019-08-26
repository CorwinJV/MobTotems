package com.corwinjv.mobtotems.items;

import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.TotemWoodBlock;
import com.corwinjv.mobtotems.gui.CarvingSelectorGui;
import com.corwinjv.mobtotems.network.ActivateKnifePacket;
import com.corwinjv.mobtotems.network.PacketHandler;
import com.corwinjv.mobtotems.network.OpenKnifeGuiMessage;
import com.corwinjv.mobtotems.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.ParticleBlockDust;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Hand;
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

    boolean spawnParticles = false;

    public CarvingKnife() {
        super();
    }

    @Override
    public void init() {
        super.init();
    }

    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (world.isRemote) {
            PacketHandler.sendToServer(new ActivateKnifePacket().setHand(hand));
            return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
        }
        return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
    }

    @SideOnly(Side.CLIENT)
    public void openGui(PlayerEntity player, int meta) {
        FMLClientHandler.instance().displayGuiScreen(player, new CarvingSelectorGui(meta));
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
        if (world.isRemote
                && entity instanceof PlayerEntity) {
            spawnParticles(stack, world, (PlayerEntity) entity);
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticles(ItemStack stack, World world, PlayerEntity player) {
        long worldTime = world.getWorldTime();

        if (spawnParticles) {
            IParticleFactory particleFactory = new ParticleBlockDust.Factory();

            Vec3d posEyes = player.getPositionEyes(1.0f);
            RayTraceResult rayTraceResult = world.rayTraceBlocks(posEyes, posEyes.add(player.getLookVec().scale(3f)));

            if (rayTraceResult != null) {
                BlockPos pos = rayTraceResult.getBlockPos();
                Block targetBlock = BlockUtils.getBlock(world, pos);

                if (targetBlock != null
                        && targetBlock instanceof TotemWoodBlock) {
                    BlockState state = world.getBlockState(pos);
                    int i = 4;

                    for (int j = 0; j < 4; ++j) {
                        for (int k = 0; k < 4; ++k) {
                            for (int l = 0; l < 4; ++l) {
                                double d0 = ((double) j + 0.5D) / 4.0D;
                                double d1 = ((double) k + 0.5D) / 4.0D;
                                double d2 = ((double) l + 0.5D) / 4.0D;

                                double speedX = d0 - 0.5D;
                                double speedY = d1 - 0.5D;
                                double speedZ = d2 - 0.5D;
                                speedX *= 0.2;
                                speedY *= 0.2;
                                speedZ *= 0.2;
                                Minecraft.getMinecraft().effectRenderer.addEffect(
                                        particleFactory.createParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(),
                                                world,
                                                (double) pos.getX() + d0,
                                                (double) pos.getY() + d1,
                                                (double) pos.getZ() + d2,
                                                speedX,
                                                speedY,
                                                speedZ,
                                                new int[]{Block.getStateId(state)}));
                            }
                        }
                    }
                }
            }
            spawnParticles = false;
        }
    }

    public void onKnifeActivated(PlayerEntity player, Hand hand) {
        if (player.world.isRemote) {
            return;
        }

        ItemStack stack = player.getHeldItem(hand);
        World world = player.world;
        Vec3d posEyes = player.getPositionEyes(1.0f);
        RayTraceResult rayTraceResult = world.rayTraceBlocks(posEyes, posEyes.add(player.getLookVec().scale(3f)));

        if (stack.getItem() instanceof CarvingKnife) {
            if (rayTraceResult != null) {
                BlockPos pos = rayTraceResult.getBlockPos();
                Block targetBlock = BlockUtils.getBlock(world, pos);

                if (targetBlock != null
                        && targetBlock instanceof TotemWoodBlock) {
                    if (world.setBlockState(pos, targetBlock.getDefaultState().withProperty(TotemWoodBlock.TOTEM_TYPE, TotemType.fromMeta(getSelectedCarving(stack))))) {
                        player.setActiveHand(hand);
                        spawnParticles = true;
                    }
                }
            } else {
                PacketHandler.sendTo(new OpenKnifeGuiMessage().setMeta(getSelectedCarving(stack)), (PlayerEntityMP) player);
            }
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 2;
    }

    //    @Override
//    public EnumAction getItemUseAction(ItemStack stack) {
//        return super.getItemUseAction(stack);
//    }

    public void setSelectedCarving(ItemStack stack, int selectedCarving) {
        if (stack.getTagCompound() == null) {
            initNbtData(stack);
        }

        NBTTagCompound tagCompound = (NBTTagCompound) stack.getTagCompound().getTag(COMPOUND_TAG);
        tagCompound.setInteger(SELECTED_CARVING_TAG, selectedCarving);
        stack.getTagCompound().setTag(COMPOUND_TAG, tagCompound);
    }

    public int getSelectedCarving(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            initNbtData(stack);
        }

        NBTTagCompound tagCompound = (NBTTagCompound) stack.getTagCompound().getTag(COMPOUND_TAG);
        return tagCompound.getInteger(SELECTED_CARVING_TAG);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        initNbtData(stack);
    }

    protected NBTTagCompound initNbtData(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger(SELECTED_CARVING_TAG, TotemType.NONE.getMeta());
        stack.getTagCompound().setTag(COMPOUND_TAG, tagCompound);
        return stack.getTagCompound();
    }
}
