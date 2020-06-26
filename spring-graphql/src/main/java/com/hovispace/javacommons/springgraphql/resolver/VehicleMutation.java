package com.hovispace.javacommons.springgraphql.resolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.hovispace.javacommons.springgraphql.entity.Vehicle;
import com.hovispace.javacommons.springgraphql.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;

public class VehicleMutation implements GraphQLMutationResolver {

    private final VehicleService _vehicleService;

    @Autowired
    public VehicleMutation(VehicleService vehicleService) {
        _vehicleService = vehicleService;
    }

    public Vehicle createVehicle(String type, String modelCode, String brandName, String launchDate) {
        return _vehicleService.createVehicle(type, modelCode, brandName, launchDate);
    }
}
