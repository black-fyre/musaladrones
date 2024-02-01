package com.example.musaladrones.medication;

import com.example.musaladrones.drone.Drone;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Name should only contain letters, numbers, '_', and '-'")
    private String name;

    @NotNull(message = "weight cannot be null")
    private Double weight;

    @NotBlank(message = "Code cannot be blank")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Code should only contain uppercase letters, numbers, and '_'")
    private String code;

    // Could be AWS S3 or GCP Cloud Storage URL
    private String imageURL;

    @ManyToMany(mappedBy = "loadedMedications")
    private Set<Drone> loadedOnDrones = new HashSet<>();


    public long getId() {
        return id;
    }

    public void setId(long id) {
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
