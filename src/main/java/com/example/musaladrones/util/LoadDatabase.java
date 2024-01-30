package com.example.musaladrones.util;

import com.example.musaladrones.drone.Drone;
import com.example.musaladrones.drone.DroneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(DroneRepository repository) {

        return args -> {
                log.info("Preloading " + repository.save(new Drone("BilboBaggins", Drone.DroneModel.CRUISERWEIGHT,
                        400, 76, Drone.DroneState.DELIVERING)));
            log.info("Preloading " + repository.save(new Drone("Bilboggins", Drone.DroneModel.LIGHTWEIGHT,
                    400, 76, Drone.DroneState.IDLE)));
        };
    }
}