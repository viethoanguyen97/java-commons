package com.hovispace.javacommons.springgraphql.service;

import com.hovispace.javacommons.springgraphql.dao.VehicleRepository;
import com.hovispace.javacommons.springgraphql.entity.Vehicle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Optional;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VehicleServiceIntegrationTest {

    @TestConfiguration
    static class VehicleServiceIntegrationTestConfiguration {

        @Bean
        public VehicleService vehicleService(VehicleRepository vehicleRepository) {
            return new VehicleService(vehicleRepository);
        }
    }

    @Resource
    private VehicleService _vehicleService;

    @Resource
    private VehicleRepository _vehicleRepository;

    @Test
    public void test_createVehicle() throws Exception {
        Vehicle vehicle = _vehicleService.createVehicle("typeTest", "modelCodeTest", "brandName", "2020-07-27");

        Vehicle actual = getOnlyElement(_vehicleRepository.findAll());
        assertThat(actual).isEqualToIgnoringGivenFields(vehicle, "formattedDate");
    }

    @Test
    public void test_getAllVehicles() throws Exception {
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setType("type1");
        vehicle1.setModelCode("model1");

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setType("type2");
        vehicle2.setModelCode("model2");

        _vehicleRepository.save(vehicle1);
        _vehicleRepository.save(vehicle2);

        assertThat(_vehicleService.getAllVehicles(1)).hasSize(1);
        assertThat(_vehicleService.getAllVehicles(2)).hasSize(2);
        assertThat(_vehicleService.getAllVehicles(3)).hasSize(2);
        assertThat(_vehicleService.getAllVehicles(2)).containsExactlyInAnyOrder(vehicle1, vehicle2);
    }

    @Test
    public void test_getVehicle() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setType("typeTest");
        vehicle.setModelCode("modelCode");

        Vehicle savedVehicle = _vehicleRepository.save(vehicle);

        Optional<Vehicle> optionalVehicle = _vehicleService.getVehicle(savedVehicle.getId());

        assertThat(optionalVehicle).isPresent();
        assertThat(optionalVehicle.get()).isEqualTo(savedVehicle);
    }
}
