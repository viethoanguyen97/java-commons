package com.hovispace.javacommons.springgraphql.resolver;

import com.hovispace.javacommons.springgraphql.entity.Vehicle;
import com.hovispace.javacommons.springgraphql.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class VehicleQuery {

    private final VehicleService _vehicleService;

    @Autowired
    public VehicleQuery(VehicleService vehicleService) {
        _vehicleService = vehicleService;
    }

    @QueryMapping
    public List<Vehicle> vehicles(@Argument int count) {
        return _vehicleService.getAllVehicles(count);
    }

    @QueryMapping
    public Optional<Vehicle> vehicle(@Argument int id) {
        return _vehicleService.getVehicle(id);
    }
}
