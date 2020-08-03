package com.hovispace.javacommons.patterns.facade.carengine;

/**
 * A facade encapsulates a complex subsystem behind a simple interface. It hides much of the complexity and makes the subsystem easy to use.
 * Also, if we need to use the complex subsystem directly, we still can do that; we aren't forced to use the facade all the time.
 *
 * Besides a much simpler interface, there's one more benefit of using this design pattern. It decouples a client implementation from the complex subsystem.
 * Thanks to this, we can make changes to the existing subsystem and don't affect a client.
 */
public class CarEngineFacade {

    private static final int DEFAULT_COOLING_TEMP = 90;
    private static final int MAX_ALLOWED_TEMP = 50;
    // for this tutorial, creating objects by new calls is accepted in this situation,
    // although we should definitely avoid doing this in real production code and use dependency injection instead, using new can lead to "hard to test" code.
    private final FuelInjector _fuelInjector = new FuelInjector();
    private final AirFlowController _airFlowController = new AirFlowController();
    private final Starter _starter = new Starter();
    private final CoolingController _coolingController = new CoolingController();
    private final CatalyticConverter _catalyticConverter = new CatalyticConverter();

    // it can be quite complex and does require some effort to start and stop the engine correctly:
    // We'll hide all the complexity in two methods: startEngine() and stopEngine().
    public void startEngine() {
        _fuelInjector.on();
        _airFlowController.takeAir();
        _fuelInjector.on();
        _fuelInjector.inject();
        _starter.start();
        _coolingController.setTemperatureUpperLimit(DEFAULT_COOLING_TEMP);
        _coolingController.run();
        _catalyticConverter.on();
    }

    public void stopEngine() {
        _fuelInjector.off();
        _catalyticConverter.off();
        _coolingController.cool(MAX_ALLOWED_TEMP);
        _coolingController.stop();
        _airFlowController.off();
    }
}
