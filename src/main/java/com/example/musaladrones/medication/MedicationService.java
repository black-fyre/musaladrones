package com.example.musaladrones.medication;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    public Medication getMedicationById(Long medicationId) {
        return medicationRepository.findById(medicationId)
                .orElseThrow(() -> new IllegalArgumentException("Medication with id " + medicationId + " not found"));
    }
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }
    public Medication createMedication(Medication medication) {
        return medicationRepository.save(medication);
    }
}
