package com.hovispace.javacommons.springgraphql.resolver;

import com.hovispace.javacommons.springgraphql.entity.Vehicle;
import com.hovispace.javacommons.springgraphql.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class VehicleMutation {

    private final VehicleService _vehicleService;

    @Autowired
    public VehicleMutation(VehicleService vehicleService) {
        _vehicleService = vehicleService;
    }

    @MutationMapping
    public Vehicle createVehicle(@Argument String type, @Argument String modelCode, @Argument String brandName, @Argument String launchDate) {
        return _vehicleService.createVehicle(type, modelCode, brandName, launchDate);
    }
}
