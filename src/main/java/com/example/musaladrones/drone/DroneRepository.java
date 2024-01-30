package com.example.musaladrones.drone;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    List<Drone> findByState(Drone.DroneState state);

}
