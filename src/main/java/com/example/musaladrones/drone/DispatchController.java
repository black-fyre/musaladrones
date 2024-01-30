package com.example.musaladrones.drone;

import com.example.musaladrones.medication.Medication;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/drones")
public class DispatchController {

    @Autowired
    private DroneService droneService;

    @PostMapping("/")
    public ResponseEntity<String> registerDrone(@Valid Drone drone) {
        droneService.registerDrone(drone);
        return new ResponseEntity<>("Drone registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/{droneId}/load")
    public ResponseEntity<String> loadDrone(@PathVariable Long droneId, @RequestBody Set<Medication> medications) {
        droneService.loadDrone(droneId, medications);
        return new ResponseEntity<>("Medications loaded successfully", HttpStatus.OK);
    }

    @GetMapping("/{droneId}/loaded-medications")
    public ResponseEntity<Set<Medication>> getLoadedMedications(@PathVariable Long droneId) {
        Set<Medication> loadedMedications = droneService.getLoadedMedications(droneId);
        return new ResponseEntity<>(loadedMedications, HttpStatus.OK);
    }

    @GetMapping("/available-for-loading")
    public ResponseEntity<List<Drone>> getAvailableDronesForLoading() {
        List<Drone> availableDrones = droneService.getAvailableDronesForLoading();
        return new ResponseEntity<>(availableDrones, HttpStatus.OK);
    }

    @GetMapping("/{droneId}/battery-level")
    public ResponseEntity<Integer> getDroneBatteryLevel(@PathVariable Long droneId) {
        int batteryLevel = droneService.getDroneBatteryCapacity(droneId);
        return new ResponseEntity<>(batteryLevel, HttpStatus.OK);
    }
}

