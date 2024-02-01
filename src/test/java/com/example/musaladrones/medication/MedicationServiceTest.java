package com.example.musaladrones.medication;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private MedicationService medicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMedicationById_WithExistingId_ShouldReturnMedication() {

        Long medicationId = 1L;
        Medication mockMedication = new Medication();
        when(medicationRepository.findById(medicationId)).thenReturn(Optional.of(mockMedication));
        Medication result = medicationService.getMedicationById(medicationId);
        assertEquals(mockMedication, result);
        verify(medicationRepository, times(1)).findById(medicationId);
    }

    @Test
    void getMedicationById_NonExistingId_ShouldThrowException() {
        Long medicationId = 1L;
        when(medicationRepository.findById(medicationId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> medicationService.getMedicationById(medicationId));
        verify(medicationRepository, times(1)).findById(medicationId);
    }

    @Test
    void getAllMedications_ShouldReturnAllMedications() {
        List<Medication> mockMedications = Arrays.asList(new Medication(), new Medication());
        when(medicationRepository.findAll()).thenReturn(mockMedications);
        List<Medication> result = medicationService.getAllMedications();
        assertEquals(mockMedications, result);
        verify(medicationRepository, times(1)).findAll();
    }

    @Test
    void getAllMedicationsByIds_ShouldReturnMedications() {
        List<Long> medicationIds = Arrays.asList(1L, 2L);
        List<Medication> mockMedications = Arrays.asList(new Medication(), new Medication());
        when(medicationRepository.findAllById(medicationIds)).thenReturn(mockMedications);
        List<Medication> result = medicationService.getAllMedicationsbyIds(medicationIds);
        assertEquals(mockMedications, result);
        verify(medicationRepository, times(1)).findAllById(medicationIds);
    }

    @Test
    void createMedication_ShouldReturnCreatedMedication() {
        Medication mockMedication = new Medication();
        when(medicationRepository.save(mockMedication)).thenReturn(mockMedication);
        Medication result = medicationService.createMedication(mockMedication);
        assertEquals(mockMedication, result);
        verify(medicationRepository, times(1)).save(mockMedication);
    }
}
