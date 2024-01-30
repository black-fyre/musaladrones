package com.example.musaladrones.dronehistory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class DroneHistory {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID droneId;

    private String message;

    private LocalDateTime timeStamp;

    public UUID getDroneId() {
        return droneId;
    }

    public void setDroneId(UUID droneId) {
        this.droneId = droneId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }


    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }


    public DroneHistory() {
    }

    public DroneHistory(UUID droneId,
                        String message,
                        LocalDateTime timeStamp) {
        this.droneId = droneId;
        this.message = message;
        this.timeStamp = timeStamp;
    }


}
