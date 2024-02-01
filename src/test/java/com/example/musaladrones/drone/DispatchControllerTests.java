package com.example.musaladrones.drone;
import com.example.musaladrones.medication.Medication;
import com.example.musaladrones.medication.MedicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DispatchController.class)
public class DispatchControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DroneService droneService;

    @MockBean
    private MedicationService medicationService;

    @Test
    void registerDrone_WithValidDroneData_ShouldCreateDroneSuccessfully() throws Exception {
        Drone drone = createDrone();
        when(droneService.registerDrone(any(Drone.class))).thenReturn(drone);
        mvc.perform(post("/api/drones/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(drone)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.weightLimit").value(drone.getWeightLimit()));
        verify(droneService, times(1)).registerDrone(any(Drone.class));
    }

    @Test
    void registerDrone_WithInvalidDroneData_ShouldValidateDataAndThrowError() throws Exception {
        Drone drone = createDrone();
        drone.setWeightLimit(600);
        mvc.perform(post("/api/drones/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(drone)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Weight limit should not exceed 500")));
    }

    @Test
    void loadDroneMedications_WithValidDroneAndMedication_ShouldReturnSuccess() throws Exception{
        //Validate that it is in LOADED state
       long fakeDroneID = ThreadLocalRandom.current().nextLong();
       List<Long> medicationIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
       Drone drone = createDrone();
       List<Medication> mockMedications = new ArrayList<>();
       when(medicationService.getAllMedicationsbyIds(medicationIds)).thenReturn(mockMedications);
       when(droneService.loadDrone(eq(fakeDroneID), anyList())).thenReturn(drone);
       mvc.perform(post("/api/drones/{droneId}/load", fakeDroneID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicationIds)))
                .andExpect(status().isOk());
        verify(droneService, times(1)).loadDrone(eq(fakeDroneID), anyList());
        verify(medicationService, times(1)).getAllMedicationsbyIds(medicationIds);

    }

    @Test
    void getLoadedMedications_WithValidDroneID_ShouldReturnMedications() throws Exception {
        // Arrange
        Long droneId = 1L;
        Set<Medication> mockMedications = new HashSet<>();
        when(droneService.getLoadedMedications(eq(droneId))).thenReturn(mockMedications);

        // Act and Assert
        mvc.perform(get("/api/drones/{droneId}/loaded-medications", droneId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(droneService, times(1)).getLoadedMedications(eq(droneId));
    }

    @Test
    void getAvailableDrones_ShouldReturnAvailableDrones() throws Exception {
        ArrayList<Drone> droneList  = new ArrayList<>();

        Drone drone = createDrone();
        droneList.add(drone);

        when(droneService.getAvailableDronesForLoading()).thenReturn(droneList);
        this.mvc.perform(get("/api/drones/available-for-loading"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(droneList))));
    }

    @Test
    void getDroneBatteryLevel_WithExistingDrone_ShouldReturnCapacity() throws Exception {
        long fakeDroneID = ThreadLocalRandom.current().nextLong();
        int  fakeDroneBatteryLevel = 25;
        when(droneService.getDroneBatteryCapacity(fakeDroneID)).thenReturn(fakeDroneBatteryLevel);
        this.mvc.perform(get("/api/drones/{droneId}/battery-level", fakeDroneID))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString( fakeDroneID +  " is " + fakeDroneBatteryLevel)));
        verify(droneService, times(1)).getDroneBatteryCapacity(eq(fakeDroneID));
    }

    @Test
    void getDroneBatteryLevel_WithNonExistentDrone_ShouldThrowError() throws Exception {
        long fakeInvalidDroneID = ThreadLocalRandom.current().nextLong();
        String errorMessage = "Drone not found";
        when(droneService.getDroneBatteryCapacity(fakeInvalidDroneID))
                .thenThrow(new IllegalArgumentException(errorMessage));
        this.mvc.perform(get("/api/drones/{droneId}/battery-level", fakeInvalidDroneID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(errorMessage)));
        verify(droneService, times(1)).getDroneBatteryCapacity(eq(fakeInvalidDroneID));
    }
    private Drone createDrone() {
        Drone drone = new Drone();
        drone.setId(1L);
        drone.setBatteryLevel(100);
        drone.setSerialNumber("ADSS");
        drone.setState(Drone.DroneState.IDLE);
        drone.setWeightLimit(50);
        drone.setModel(Drone.DroneModel.LIGHTWEIGHT);
        drone.setLoadedMedications(new HashSet<>());
        return drone;
    }
}
