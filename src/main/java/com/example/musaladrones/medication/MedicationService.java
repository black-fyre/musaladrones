package com.example.musaladrones.medication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationService {


    private final MedicationRepository medicationRepository;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public Medication getMedicationById(Long medicationId) {
        return medicationRepository.findById(medicationId)
                .orElseThrow(() -> new IllegalArgumentException("Medication with id " + medicationId + " not found"));
    }
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    public List<Medication> getAllMedicationsbyIds(List<Long> medicationIds) {
        return  medicationRepository.findAllById(medicationIds);
    }
    public Medication createMedication(Medication medication) {
        return medicationRepository.save(medication);
    }
}
