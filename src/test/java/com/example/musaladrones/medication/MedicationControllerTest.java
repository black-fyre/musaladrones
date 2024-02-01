package com.example.musaladrones.medication;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;


import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicationController.class)
class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicationService medicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllMedications_ShouldReturnMedications() throws Exception {
        mockMvc.perform(get("/api/medications/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(medicationService, times(1)).getAllMedications();
    }

    @Test
    void createMedication_WithValidData_ShouldReturnCreatedStatus() throws Exception {
        Medication mockMedication = createMedication();
        when(medicationService.createMedication(any())).thenReturn(mockMedication);
        mockMvc.perform(post("/api/medications/")
                        .content(objectMapper.writeValueAsString(mockMedication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(mockMedication.getName())));
        verify(medicationService, times(1)).createMedication(any());
    }

    @Test
    void createMedication_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Medication invalidMedication = createMedication();
        invalidMedication.setName("**");// This medication is intentionally left invalid
        mockMvc.perform(post("/api/medications/")
                        .content(objectMapper.writeValueAsString(invalidMedication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("name")));
        verifyNoMoreInteractions(medicationService);
    }
    private Medication createMedication() {
        Medication medication = new Medication();
        medication.setId(1L);
        medication.setCode("AS002");
        medication.setWeight(10);
        medication.setName("Aspirin");
        return medication;
        }
}
