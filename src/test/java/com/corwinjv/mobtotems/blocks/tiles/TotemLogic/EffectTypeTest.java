package com.corwinjv.mobtotems.blocks.tiles.TotemLogic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by CorwinJV on 3/15/2017.
 */
@PowerMockIgnore({"javax.management.*"})
@RunWith(PowerMockRunner.class)
public class EffectTypeTest {
    TotemLogic.EffectType effectType;

    @Before
    public void setUp() throws Exception {
        effectType = TotemLogic.EffectType.EFFECT;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void valueOf() {
        assertEquals(TotemLogic.EffectType.EFFECT, effectType.valueOf("EFFECT"));
    }

    @Test
    public void values() {
        assertNotNull(effectType.values());
    }
}
