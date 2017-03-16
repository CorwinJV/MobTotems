package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.annotation.Nonnull;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by CorwinJV on 3/15/2017.
 */
@PowerMockIgnore({"javax.management.*"})
@RunWith(PowerMockRunner.class)
public class TotemLogicTest {
    TotemLogic totemLogic;

    @Mock
    World world;
    @Mock
    BlockPos pos;
    @Mock
    Modifiers modifiers;

    @Before
    public void setUp() throws Exception {
        totemLogic = new TotemLogic() {
            @Override
            public List<ItemStack> getCost() {
                return null;
            }

            @Nonnull
            @Override
            public EffectType getEffectType() {
                return null;
            }
        };
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void adjustModifiers() throws Exception {
        assertEquals(modifiers, totemLogic.adjustModifiers(modifiers));
    }

    @Test
    public void performEffect() throws Exception {
        totemLogic.performEffect(world, pos, modifiers);
    }

}
