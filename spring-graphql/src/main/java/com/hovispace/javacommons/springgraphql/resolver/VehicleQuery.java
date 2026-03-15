package com.hovispace.javacommons.springgraphql.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.hovispace.javacommons.springgraphql.entity.Vehicle;
import com.hovispace.javacommons.springgraphql.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class VehicleQuery implements GraphQLQueryResolver {

    private final VehicleService _vehicleService;

    @Autowired
    public VehicleQuery(VehicleService vehicleService) {
        _vehicleService = vehicleService;
    }

    public List<Vehicle> getVehicles(int count) {
        return _vehicleService.getAllVehicles(count);
    }

    public Optional<Vehicle> getVehicle(int id) {
        return _vehicleService.getVehicle(id);
    }
}
