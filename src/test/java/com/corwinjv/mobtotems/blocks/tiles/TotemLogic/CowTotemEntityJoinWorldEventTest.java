package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import com.corwinjv.mobtotems.TotemHelper;
import com.corwinjv.mobtotems.blocks.TotemType;
import com.corwinjv.mobtotems.blocks.tiles.OfferingBoxTileEntity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by CorwinJV on 3/15/2017.
 */
@PowerMockIgnore({"javax.management.*"})
@PrepareForTest({EntityJoinWorldEvent.class, World.class, TotemHelper.class})
@RunWith(PowerMockRunner.class)
public class CowTotemEntityJoinWorldEventTest {
    CowLogic.CowTotemEntityJoinWorldEvent cowTotemEntityJoinWorldEvent;

    @Mock
    EntityJoinWorldEvent event;
    @Mock
    World world;
    @Mock
    OfferingBoxTileEntity mockOfferingBoxTE;
    @Mock
    Modifiers modifiers;
    @Mock
    EntityAnimal mob;

    @Before
    public void setUp() throws Exception {
        mockStatic(TotemHelper.class);

        cowTotemEntityJoinWorldEvent = new CowLogic.CowTotemEntityJoinWorldEvent();
        List<TileEntity> loadedTileEntityList = new ArrayList<>();
        Whitebox.setInternalState(world, "loadedTileEntityList", loadedTileEntityList);
        when(event.getWorld()).thenReturn(world);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void onEntityJoinWorldEventNoMatch() throws Exception {
        cowTotemEntityJoinWorldEvent.onEntityJoinWorldEvent(event);
        verify(event, never()).setCanceled(eq(true));
    }

    @Test
    public void onEntityJoinWorldEventMatch() throws Exception {
        when(mockOfferingBoxTE.getChargeLevel()).thenReturn(1);
        when(mockOfferingBoxTE.getPos()).thenReturn(new BlockPos(0, 64, 0));
        world.loadedTileEntityList.add(mockOfferingBoxTE);

        PowerMockito.when(TotemHelper.hasTotemType((eq(mockOfferingBoxTE)), eq(TotemType.COW))).thenReturn(true);
        PowerMockito.when(TotemHelper.getModifiers(eq(world), eq(mockOfferingBoxTE))).thenReturn(modifiers);

        when(mob.getPosition()).thenReturn(new BlockPos(1, 64, 1));
        when(event.getEntity()).thenReturn(mob);

        cowTotemEntityJoinWorldEvent.onEntityJoinWorldEvent(event);
        verify(event).setCanceled(eq(true));
    }

}
