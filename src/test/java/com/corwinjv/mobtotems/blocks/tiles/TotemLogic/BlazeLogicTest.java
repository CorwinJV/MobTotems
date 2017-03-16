package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by CorwinJV on 3/14/2017.
 */
@SuppressStaticInitializationFor({"net.minecraft.init.Blocks", "net.minecraft.init.Items"})
@PowerMockIgnore({"javax.management.*"})
@PrepareForTest({Items.class})
@RunWith(PowerMockRunner.class)
public class BlazeLogicTest {
    BlazeLogic blazeLogic = null;
    @Mock
    World world;
    @Mock
    BlockPos blockPos;
    @Mock
    Modifiers modifiers;

    // Hack to get around isImmuneToFire() (a minecraft method) being final
    class TestEntityMob extends EntityMob {
        public TestEntityMob(World worldIn) {
            super(worldIn);
            isImmuneToFire = false;
        }
    }

    @Mock
    TestEntityMob entityTargetInRange;
    @Mock
    TestEntityMob entityTargetOutOfRange;

    @Before
    public void setUp() throws Exception {
        mockStatic(Items.class);

        // Normal mockito stuff
        blazeLogic = new BlazeLogic();

        when(blockPos.getX()).thenReturn(100);
        when(blockPos.getY()).thenReturn(64);
        when(blockPos.getZ()).thenReturn(100);

        when(entityTargetInRange.getPosition()).thenReturn(new BlockPos(102, 64, 102));
        when(entityTargetOutOfRange.getPosition()).thenReturn(new BlockPos(182, 64, 182));

        List<Entity> entityList = new ArrayList<>();
        entityList.add(entityTargetInRange);
        entityList.add(entityTargetOutOfRange);

        when(world.getEntitiesWithinAABB(eq(Entity.class), any(AxisAlignedBB.class))).thenReturn(entityList);
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCost() throws Exception {
        Whitebox.setInternalState(Items.class, "COAL", new Item().setUnlocalizedName("coal"));

        List<ItemStack> items = blazeLogic.getCost();
        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getCount());

        // If it's null, then it's a field we haven't mocked and it's wrong.
        assertNotNull(items.get(0).getItem());

        //FMLLog.log(Level.INFO, "%s", items.get(0).getItem().getUnlocalizedName());
    }

    @Test
    public void getEffectType() throws Exception {
        assertEquals(TotemLogic.EffectType.EFFECT, blazeLogic.getEffectType());
    }

    @Test
    public void performEffect() throws Exception {
        blazeLogic.performEffect(world, blockPos, modifiers);

        verify(entityTargetInRange).setFire(anyInt());
        verify(entityTargetOutOfRange, never()).setFire(anyInt());
    }
}
