package com.example.musaladrones;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.musaladrones.drone.DispatchController;
import com.example.musaladrones.drone.Drone;
import com.example.musaladrones.drone.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DispatchController.class)
@AutoConfigureMockMvc
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
        drone.setBatteryCapacity(80);
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
        drone.setBatteryCapacity(80);
        drone.setState(Drone.DroneState.IDLE);

        mvc.perform(post("/api/drones/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(drone)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Drone registered successfully")));
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




//    @Test
//    void shouldAvailableDrones_ShouldCallCorrectMethod() throws Exception {
//        ArrayList<Drone> droneList  = new ArrayList<>();
//
//        Drone drone = new Drone();
//        drone.setSerialNumber("ABC123");
//        drone.setModel(Drone.DroneModel.LIGHTWEIGHT);
//        drone.setWeightLimit(300);
//        drone.setBatteryCapacity(56);
//        drone.setState(Drone.DroneState.DELIVERING);
//        droneList.add(drone);
//
//        when(droneService.getAvailableDronesForLoading()).thenReturn(droneList);
//        this.mvc.perform(get("/api/drones/available-for-loading"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("56")));
//    }

    public void getDroneBatteryCapacity_WithValidDrone_ShouldReturnCapacity() throws Exception{

    }

    public void getDroneBatteryCapacity_WithInValidDrone_ShouldThrowError() throws Exception{

    }

    public void getDroneById_WithValidDrone_ShouldReturnDrone() throws Exception{

    }

    public void checkBatteryLevelsAndLog_ShouldLogBatteryLevels() throws Exception{

    }
}
