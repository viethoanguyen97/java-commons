package com.hovispace.javacommons.springgraphql.dao;

import com.hovispace.javacommons.springgraphql.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

}
