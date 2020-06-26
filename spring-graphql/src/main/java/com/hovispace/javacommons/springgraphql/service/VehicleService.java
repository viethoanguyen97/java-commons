package com.hovispace.javacommons.springgraphql.service;

import com.hovispace.javacommons.springgraphql.dao.VehicleRepository;
import com.hovispace.javacommons.springgraphql.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class VehicleService {

    private final VehicleRepository _vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        _vehicleRepository = vehicleRepository;
    }

    @Transactional
    public Vehicle createVehicle(String type, String modelCode, String brandName, String launchDate) {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(type);
        vehicle.setModelCode(modelCode);
        vehicle.setBrandName(brandName);
        vehicle.setLaunchDate(LocalDate.parse(launchDate));

        return _vehicleRepository.save(vehicle);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> getAllVehicles(int count) {
        return _vehicleRepository.findAll().stream().limit(count).collect(toList());
    }

    @Transactional(readOnly = true)
    public Optional<Vehicle> getVehicle(int id) {
        return _vehicleRepository.findById(id);
    }
}
