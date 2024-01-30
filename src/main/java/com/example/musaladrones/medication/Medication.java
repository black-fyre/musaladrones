package com.example.musaladrones.medication;

import com.example.musaladrones.drone.Drone;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Medication {
    @Id
    @GeneratedValue
    private UUID id;

    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Name should only contain letters, numbers, '_', and '-'")
    private String name;

    private double weight;

    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Code should only contain uppercase letters, numbers, and '_'")
    private String code;

    // Consider using appropriate types or libraries for handling images
    private String imageURL;

    @ManyToMany(mappedBy = "loadedMedications")
    private Set<Drone> loadedOnDrones = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Medication() {

    }

    public Medication(String name,
                      double weight,
                      String code,
                      String imageURL) {
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.imageURL = imageURL;
    }
}
