package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by CorwinJV on 3/14/2017.
 */
@RunWith(MockitoJUnitRunner.class)
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
        blazeLogic = new BlazeLogic();

        when(blockPos.getX()).thenReturn(100);
        when(blockPos.getY()).thenReturn(64);
        when(blockPos.getZ()).thenReturn(100);

        when(entityTargetInRange.getPosition()).thenReturn(new BlockPos(102, 64, 102));
        when(entityTargetOutOfRange.getPosition()).thenReturn(new BlockPos(182, 64, 182));

        List<Entity> entityList = new ArrayList<>();
        entityList.add(entityTargetInRange);
        entityList.add(entityTargetOutOfRange);

        when(world.getEntitiesWithinAABB(eq(Entity.class), Mockito.any(AxisAlignedBB.class))).thenReturn(entityList);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCost() throws Exception {
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
