package com.hovispace.javacommons.springgraphql.service;

import com.hovispace.javacommons.springgraphql.dao.VehicleRepository;
import com.hovispace.javacommons.springgraphql.entity.Vehicle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VehicleServiceUnitTest {

    @Mock
    private VehicleRepository _vehicleRepository;

    @Test
    public void test_that_getAllVehicles_returns_expected_data() throws Exception {
        VehicleService vehicleService = new VehicleService(_vehicleRepository);
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();

        when(_vehicleRepository.findAll()).thenReturn(asList(vehicle1, vehicle2));

        assertThat(vehicleService.getAllVehicles(1)).containsExactly(vehicle1);
        assertThat(vehicleService.getAllVehicles(2)).containsExactly(vehicle1, vehicle2);
    }

    @Test
    public void test_that_getVehicle_returns_vehicle_with_exact_id() throws Exception {
        VehicleService vehicleService = new VehicleService(_vehicleRepository);
        Vehicle vehicle = new Vehicle();

        when(_vehicleRepository.findById(eq(1))).thenReturn(Optional.of(vehicle));

        assertThat(vehicleService.getVehicle(1)).isPresent();
        assertThat(vehicleService.getVehicle(1).get()).isEqualTo(vehicle);
    }

    @Test
    public void test_that_createVehicle_save_vehicle_with_exact_properties() throws Exception {
        VehicleService vehicleService = new VehicleService(_vehicleRepository);

        vehicleService.createVehicle("type", "modelCode", "brandName", "2020-06-27");

        ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);
        verify(_vehicleRepository).save(vehicleArgumentCaptor.capture());
        Vehicle vehicle = vehicleArgumentCaptor.getValue();

        assertThat(vehicle.getType()).isEqualTo("type");
        assertThat(vehicle.getModelCode()).isEqualTo("modelCode");
        assertThat(vehicle.getBrandName()).isEqualTo("brandName");
        assertThat(vehicle.getLaunchDate()).isEqualTo(LocalDate.parse("2020-06-27"));
    }
}
