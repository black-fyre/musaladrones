package com.example.musaladrones.drone;

import com.example.musaladrones.dronehistory.DroneHistory;
import com.example.musaladrones.medication.Medication;
import com.example.musaladrones.dronehistory.DroneHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class DroneService {

    private final DroneRepository droneRepository;

    private final DroneHistoryRepository droneHistoryRepository;
    ++
    @Autowired
    public DroneService(DroneRepository droneRepository,
                        DroneHistoryRepository droneHistoryRepository) {
        this.droneRepository = droneRepository;
        this.droneHistoryRepository = droneHistoryRepository;
    }

    public void registerDrone(Drone drone) {
        // Additional validation or business logic can be added here
        droneRepository.save(drone);
    }

    public Drone loadDrone(Long droneId, List<Medication> medications) {
        Drone drone = getDroneById(droneId);

        if (checkIfWeightAboveDroneLimit(drone, medications)) {
            throw new IllegalArgumentException("Drone cannot be loaded with more weight than it can carry");
        }

        if (drone.getBatteryLevel() < 25) {
            throw new IllegalArgumentException("Drone battery level is below 25%, cannot be in LOADING state");
        }

        drone.setState(Drone.DroneState.LOADING);

        drone.getLoadedMedications().addAll(medications);

        drone.setState(Drone.DroneState.LOADED);

        // Additional logic like updating battery level can be added here
        return droneRepository.save(drone);
    }
    public Set<Medication> getLoadedMedications(Long droneId) {
        Drone drone = getDroneById(droneId);
        return drone.getLoadedMedications();
    }

    public List<Drone> getAvailableDronesForLoading() {
        // Assumption: Only drones with state IDLE are available for loading
        return droneRepository.findByState(Drone.DroneState.IDLE);
    }

    public int getDroneBatteryCapacity(Long droneId) {
        Drone drone = getDroneById(droneId);
        return drone.getBatteryLevel();

    }

    private Drone getDroneById(Long droneId) {
        return droneRepository.findById(droneId)
                .orElseThrow(() -> new IllegalArgumentException("Drone with id " + droneId + " not found"));
    }

    @Scheduled(cron = "0 0 * * * ?") // Runs every hour
    public void checkBatteryLevelsAndLog() {
        List<Drone> drones = droneRepository.findAll();

        for (Drone drone : drones) {
            if (drone.getBatteryLevel() < 25) {
                createDroneHistoryLog(drone, "Battery level below 25%");
            }
        }
    }

    private void createDroneHistoryLog(Drone drone, String message) {
        DroneHistory droneHistory = new DroneHistory();
        droneHistory.setDroneId(drone.getId());
        droneHistory.setMessage(message);
        droneHistory.setTimeStamp(LocalDateTime.now());
        droneHistoryRepository.save(droneHistory);
    }

    private Boolean checkIfWeightAboveDroneLimit(Drone drone, List<Medication> medications) {
        double totalWeight = medications.stream().mapToDouble(Medication::getWeight).sum();
        return totalWeight > drone.getWeightLimit();
    }
}

