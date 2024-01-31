package com.example.musaladrones.drone;

import com.example.musaladrones.medication.Medication;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Size(max = 100)
    @NotBlank(message = "Serial number is required")
    private String serialNumber;

    @NotNull(message = "Model is required")
    @Enumerated(EnumType.STRING)
    private DroneModel model;

    @NotNull(message = "Battery level is required")
    @Max(value = 100, message = "Battery percent cannot exceed 100")
    @Min(value = 0, message = "Battery percent cannot be lower than 100")
    private Integer batteryLevel;

    @NotNull(message = "State is required")
    @Enumerated(EnumType.STRING)
    private DroneState state;


    @NotNull(message = "Weight limit is required")
    @Max(value = 500, message = "Weight limit should not exceed 500 grams")
    @Min(value = 0, message = "Weight limit should not be lower than 1 gram")
    private Integer weightLimit;



    @ManyToMany
    @JoinTable(
            name = "drone_medication",
            joinColumns = @JoinColumn(name = "drone_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    private Set<Medication> loadedMedications = new HashSet<>();

    public Drone() {

    }

    public Drone(String serialNumber,
                 DroneModel model,
                 int weightLimit,
                 int batteryLevel,
                 DroneState state) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weightLimit = weightLimit;
        this.batteryLevel = batteryLevel;
        this.state = state;
    }


    public enum DroneModel {
        LIGHTWEIGHT,
        MIDDLEWEIGHT,
        CRUISERWEIGHT,
        HEAVYWEIGHT
    }

    public enum DroneState {
        IDLE,
        LOADING,
        LOADED,
        DELIVERING,
        DELIVERED,
        RETURNING
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DroneModel getModel() {
        return model;
    }

    public void setModel(DroneModel model) {
        this.model = model;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryCapacity) {
        this.batteryLevel = batteryCapacity;
    }

    public DroneState getState() {
        return state;
    }

    public void setState(DroneState state) {
        this.state = state;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
    }

    public Set<Medication> getLoadedMedications() {
        return loadedMedications;
    }

    public void setLoadedMedications(Set<Medication> loadedMedications) {
        this.loadedMedications = loadedMedications;
    }
}
