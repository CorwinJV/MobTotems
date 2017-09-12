package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.assertEquals;

/**
 * Created by CorwinJV on 3/16/2017.
 */
@PowerMockIgnore({"javax.management.*"})
@RunWith(PowerMockRunner.class)
public class ModifiersTest {
    Modifiers modifiers;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void Modifiers() throws Exception {
        modifiers = new Modifiers();
        assertEquals(0, modifiers.damage, 0);
        assertEquals(0, modifiers.range, 0);
        assertEquals(0, modifiers.speed, 0);
    }
}
