package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by CorwinJV on 3/14/2017.
 */
@SuppressStaticInitializationFor({"net.minecraft.init.Blocks", "net.minecraft.init.Items", "net.minecraft.block.SoundType"})
@PowerMockIgnore({"javax.management.*"})
@PrepareForTest({Blocks.class, Item.class})
@RunWith(PowerMockRunner.class)
public class CowLogicTest {
    CowLogic cowLogic = null;

    class MockBlockGrass extends BlockTallGrass {
        MockBlockGrass() {
            super();
        }
    }

    @Before
    public void setUp() throws Exception {
        mockStatic(Blocks.class);
        mockStatic(Item.class);
        cowLogic = new CowLogic();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getCost() throws Exception {
        MockBlockGrass mockBlockGrass = new MockBlockGrass();
        Whitebox.setInternalState(Blocks.class, "TALLGRASS", mockBlockGrass);
        when(Item.getItemFromBlock(eq(mockBlockGrass))).thenReturn(new Item().setUnlocalizedName("tallgrass"));

        List<ItemStack> items = cowLogic.getCost();
        assertEquals(1, items.size());
        assertEquals(4, items.get(0).getCount());

        // If it's null, then it's a field we haven't mocked and it's wrong.
        assertNotNull(items.get(0).getItem());
    }

    @Test
    public void getEffectType() throws Exception {
        assertEquals(TotemLogic.EffectType.EFFECT, cowLogic.getEffectType());
    }
}
