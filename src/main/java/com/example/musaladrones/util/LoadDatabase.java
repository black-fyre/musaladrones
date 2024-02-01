package com.example.musaladrones.util;

import com.example.musaladrones.drone.Drone;
import com.example.musaladrones.drone.DroneRepository;
import com.example.musaladrones.medication.Medication;
import com.example.musaladrones.medication.MedicationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(DroneRepository droneRepository, MedicationRepository medicationRepository) {
        return args -> {
//             Preload 5 drones
            for (int i = 1; i <= 5; i++) {
                Drone drone = new Drone();
                drone.setSerialNumber("Drone-" + i);
                drone.setModel(Drone.DroneModel.LIGHTWEIGHT);
                drone.setWeightLimit(500);
                drone.setBatteryLevel(100);
                drone.setState(Drone.DroneState.IDLE);
                droneRepository.save(drone);
            }

//            // Preload 5 medications
            for (int i = 1; i <= 5; i++) {
                Medication medication = new Medication();
                medication.setName("Medication-" + i);
                medication.setWeight(50);
                medication.setCode("MED" + i);
                medication.setImageURL("https://example.com/medication-" + i + ".jpg");
                medicationRepository.save(medication);
            }
        };
    }
}