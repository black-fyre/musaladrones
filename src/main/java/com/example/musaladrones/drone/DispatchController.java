package com.example.musaladrones.drone;

import com.example.musaladrones.medication.Medication;
import com.example.musaladrones.medication.MedicationService;
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

    private final DroneService droneService;

    private final MedicationService medicationService;

    @Autowired
    public DispatchController(DroneService droneService,
                              MedicationService medicationService) {
        this.droneService = droneService;
        this.medicationService = medicationService;
    }
    @PostMapping("/")
    public ResponseEntity<String> registerDrone(@RequestBody @Valid Drone drone) {
        droneService.registerDrone(drone);
        return new ResponseEntity<>("Drone registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/{droneId}/load")
    public ResponseEntity<Drone> loadDrone(@PathVariable Long droneId, @RequestBody List<Long> medicationIds) {
        List<Medication> medications = medicationService.getAllMedicationsbyIds(medicationIds);
        Drone loadedDrone = droneService.loadDrone(droneId, medications);
        return new ResponseEntity<>(loadedDrone, HttpStatus.OK);
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
    public ResponseEntity<String> getDroneBatteryLevel(@PathVariable Long droneId) {
        try {
            int batteryLevel = droneService.getDroneBatteryCapacity(droneId);
            return new ResponseEntity<>("The battery level for drone " + droneId + " is " + batteryLevel, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
}

