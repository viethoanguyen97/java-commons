package com.hovispace.javacommons.patterns.facade.carengine;

public class CoolingController {

    private int _defaultCoolingTemp;

    public void setTemperatureUpperLimit(int defaultCoolingTemp) {
        _defaultCoolingTemp = defaultCoolingTemp;
    }

    public void run() {}

    public void cool(int maxAllowedTemp) {

    }

    public void stop() {}
}
