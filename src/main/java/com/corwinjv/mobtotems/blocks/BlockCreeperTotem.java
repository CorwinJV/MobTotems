package com.corwinjv.mobtotems.blocks;

import com.corwinjv.mobtotems.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.Level;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by vanhc011 on 9/1/14.
 */
public class BlockCreeperTotem extends BlockMT
{
    public BlockCreeperTotem()
    {
        super();
        //setBlockName("creeperTotem");
        //setTickRandomly(true);

        //World clientSideWorld = Minecraft.getMinecraft().theWorld;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
//        super.updateTick(worldIn, pos, state, rand);
//        //FMLLog.log(Reference.MOD_NAME, Level.INFO, String.format("updateTick on block at %d, %d, %d", x, y, z));
//
//        // Find all mobs within bounding box that match the type creeper...
//        // Set those mobs on fire.
//        Class creeperClass = (Class) EntityList.stringToClassMapping.get("Creeper");
//        List entitiesWithinAABB = worldIn.getEntitiesWithinAABB(creeperClass,
//                AxisAlignedBB.fromBounds(pos.getX() - 16,
//                                            pos.getY() - 16,
//                                            pos.getZ() - 16,
//                                            pos.getX() + 16,
//                                            pos.getY() + 16,
//                                            pos.getZ() + 16));
//
//        //FMLLog.log(Reference.MOD_NAME, Level.INFO, String.format("Entities found: %d", entitiesWithinAABB.size()));
//
//        for(int i = 0; i < entitiesWithinAABB.size(); i++)
//        {
//            EntityLivingBase creeperToBurn = (EntityLivingBase)entitiesWithinAABB.get(i);
//            creeperToBurn.setFire(3);
//        }
//
//        worldIn.scheduleBlockUpdate(pos, this, tickRate(worldIn), 0);
    }

//    @Override
//    public int tickRate(World world)
//    {
//        return 10;
//    }
}
