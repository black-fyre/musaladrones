package com.example.musaladrones;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.musaladrones.drone.DispatchController;
import com.example.musaladrones.drone.Drone;
import com.example.musaladrones.drone.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DispatchController.class)
public class DispatchControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DroneService droneService;

    @Test
    public void registerDrone_WithValidDroneData_ShouldCreateDroneSuccessfully() throws Exception {
        Drone drone = new Drone();
        drone.setSerialNumber("ABC123");
        drone.setModel(Drone.DroneModel.LIGHTWEIGHT);
        drone.setWeightLimit(200);
        drone.setBatteryLevel(80);
        drone.setState(Drone.DroneState.IDLE);

        mvc.perform(post("/api/drones/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(drone)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Drone registered successfully")));
    }

    @Test
    public void registerDrone_WithInvalidDroneData_ShouldValidateDataAndThrowError() throws Exception {
        Drone drone = new Drone();
        drone.setSerialNumber("ABC123");
        drone.setModel(Drone.DroneModel.LIGHTWEIGHT);
        drone.setWeightLimit(5500);
        drone.setBatteryLevel(80);
        drone.setState(Drone.DroneState.IDLE);

        mvc.perform(post("/api/drones/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(drone)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Weight limit should not exceed 500")));
    }

    public void loadDroneMedications_WithValidDroneAndMedication_ShouldReturnSuccess() throws Exception{
        //Validate that it is in LOADED state

    }
    public void loadDroneMedications_WithInvalidDroneAndMedication_ShouldThrowError() throws Exception{

    }

    public void loadDroneMedications_WithValidDroneAndInvalidMedication_ShouldThrowError() throws Exception{

    }
    public void loadDroneMedications_WithExcessWeight_ShouldThrowError() throws Exception{

    }

    public void loadDroneMedications_WithBatteryLevelBelow25_ShouldThrowError() throws Exception{

    }

    public void getLoadedMedications_WithValidDroneID_ShouldReturnMedications() throws Exception{
    }

    public void getLoadedMedications_WithInvalidDroneID_ShouldThrowError() throws Exception{
    }

    @Test
    void getAvailableDrones_ShouldReturnDrones() throws Exception {
        ArrayList<Drone> droneList  = new ArrayList<>();

        Drone drone = new Drone();
        drone.setSerialNumber("ABC123");
        drone.setModel(Drone.DroneModel.LIGHTWEIGHT);
        drone.setWeightLimit(300);
        drone.setBatteryLevel(56);
        drone.setState(Drone.DroneState.IDLE);
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
        this.mvc.perform(get("/api/drones/"+ fakeDroneID +"/battery-level"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString( fakeDroneID +  " is " + fakeDroneBatteryLevel)));
    }

    @Test
    void getDroneBatteryLevel_WithNonExistentDrone_ShouldThrowError() throws Exception {
        long fakeInvalidDroneID = ThreadLocalRandom.current().nextLong();
        String errorMessage = "Drone not found";
        when(droneService.getDroneBatteryCapacity(fakeInvalidDroneID))
                .thenThrow(new IllegalArgumentException(errorMessage));
        this.mvc.perform(get("/api/drones/" + fakeInvalidDroneID + "/battery-level"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(errorMessage)));
    }

    @Test
    void getDroneBatteryLevel_WithInvalidIDFormat_ShouldThrowError() throws Exception {

    }
}
