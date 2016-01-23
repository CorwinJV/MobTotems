package com.corwinjv.mobtotems.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Random;

/**
 * Created by CorwinJV on 9/1/14.
 */
public class CreeperTotem extends BaseBlock
{
    public CreeperTotem()
    {
        super();
        setTickRandomly(true);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }


    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);
        //FMLLog.log(Level.INFO, "Update tick on block at {%d, %d, %d}", pos.getX(), pos.getY(), pos.getZ());

        // Find all mobs within bounding box that match the type creeper...
        // Set those mobs on fire.
        Class creeperClass = (Class) EntityList.stringToClassMapping.get("Creeper");
        List entitiesWithinAABB = worldIn.getEntitiesWithinAABB(creeperClass,
                AxisAlignedBB.fromBounds(pos.getX() - 16,
                                            pos.getY() - 16,
                                            pos.getZ() - 16,
                                            pos.getX() + 16,
                                            pos.getY() + 16,
                                            pos.getZ() + 16));

        //FMLLog.log(Reference.MOD_NAME, Level.INFO, String.format("Entities found: %d", entitiesWithinAABB.size()));

        for(int i = 0; i < entitiesWithinAABB.size(); i++)
        {
            EntityLivingBase creeperToBurn = (EntityLivingBase)entitiesWithinAABB.get(i);
            if(!creeperToBurn.isBurning())
            {
                creeperToBurn.setFire(3);
            }
        }

        worldIn.scheduleBlockUpdate(pos, this, tickRate(worldIn), 0);
        setTickRandomly(false);
    }

    @Override
    public int tickRate(World world)
    {
        return 20;
    }
}
