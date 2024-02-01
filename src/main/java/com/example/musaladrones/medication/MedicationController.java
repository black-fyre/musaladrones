package com.example.musaladrones.medication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping(path = "/api/medications", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class MedicationController {

    private final MedicationService medicationService;

    @Autowired
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Medication>> getAllMedications() {
        List<Medication> medications = medicationService.getAllMedications();
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Medication> createMedication(@RequestBody @Valid Medication medication) {
        Medication createdMedication = medicationService.createMedication(medication);
        return new ResponseEntity<>(createdMedication, HttpStatus.CREATED);
    }
}
