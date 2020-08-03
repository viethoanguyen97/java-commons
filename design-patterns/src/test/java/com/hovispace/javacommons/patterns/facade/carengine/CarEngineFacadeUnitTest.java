package com.hovispace.javacommons.patterns.facade.carengine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CarEngineFacadeUnitTest {

    @Test
    public void test_start_engine() throws Exception {
        CarEngineFacade carEngineFacade = Mockito.mock(CarEngineFacade.class);
        carEngineFacade.startEngine();
    }

    @Test
    public void test_stop_engine() {

    }
}