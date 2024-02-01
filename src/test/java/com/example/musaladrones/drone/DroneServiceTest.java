package com.example.musaladrones.drone;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.musaladrones.dronehistory.DroneHistory;
import com.example.musaladrones.dronehistory.DroneHistoryRepository;
import com.example.musaladrones.medication.Medication;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private DroneHistoryRepository droneHistoryRepository;

    @InjectMocks
    private DroneService droneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerDrone_ShouldCallRepositorySaveMethodOnce() {
        Drone drone = new Drone();
        droneService.registerDrone(drone);
        verify(droneRepository, times(1)).save(drone);
    }

    @Test
    void loadDrone_WithValidData_ShouldReturnValidData() {
        Drone drone = createDrone();
        List<Medication> medications = Arrays.asList(
                new Medication("Aspirin", 0.1, "ASP001", "https://example.com/aspirin-image.jpg"),
                new Medication("Ibuprofen", 0.2, "IBP002", "https://example.com/ibuprofen-image.jpg")
        );

        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        when(droneRepository.save(any())).thenReturn(drone);

        Drone loadedDrone = droneService.loadDrone(1L, medications);

        assertEquals(Drone.DroneState.LOADED, loadedDrone.getState());
        assertEquals(2, loadedDrone.getLoadedMedications().size());
        verify(droneRepository, times(1)).findById(1L);
        verify(droneRepository, times(1)).save(drone);
    }

    @Test
    void loadDrone_WithWeightAboveLimit_ShouldThrowException() {
        Drone drone = createDrone();
        List<Medication> medications = Arrays.asList(
                new Medication("Aspirin", 30, "ASP001", "https://example.com/aspirin-image.jpg"),
                new Medication("Ibuprofen", 30, "IBP002", "https://example.com/ibuprofen-image.jpg")
        );

        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));

        assertThrows(IllegalArgumentException.class, () -> droneService.loadDrone(1L, medications));
        assertEquals(Drone.DroneState.IDLE, drone.getState());
        verify(droneRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void loadDrone_WithBatteryBelow25_ShouldThrowException() {
        Drone drone = createDrone();
        drone.setBatteryLevel(20);

        List<Medication> medications = Arrays.asList(
                new Medication("Aspirin", 10, "ASP001", "https://example.com/aspirin-image.jpg"),
                new Medication("Ibuprofen", 30, "IBP002", "https://example.com/ibuprofen-image.jpg")
        );

        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));

        assertThrows(IllegalArgumentException.class, () -> droneService.loadDrone(1L, medications));
        assertEquals(Drone.DroneState.IDLE, drone.getState());
        verify(droneRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void getLoadedMedications_ShouldReturnCorrectNumberOfMedications() {
        Drone drone = createDrone();
        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));

        Set<Medication> loadedMedications = droneService.getLoadedMedications(1L);

        assertEquals(0, loadedMedications.size());
        verify(droneRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void getAvailableDronesForLoading_ShouldReturnCorrectNumberOfDrones() {
        when(droneRepository.findByState(Drone.DroneState.IDLE)).thenReturn(Arrays.asList(createDrone()));

        List<Drone> availableDrones = droneService.getAvailableDronesForLoading();

        assertEquals(1, availableDrones.size());
        verify(droneRepository, times(1)).findByState(Drone.DroneState.IDLE);
        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void getDroneBatteryCapacity_WithExistingID_ShouldReturnCorrectCapacity() {
        Drone drone = createDrone();
        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));

        int batteryCapacity = droneService.getDroneBatteryCapacity(1L);

        assertEquals(100, batteryCapacity);
        verify(droneRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(droneRepository, droneHistoryRepository);
    }

    @Test
    void checkBatteryLevelsAndLog_WithExistingDrones_ShouldSaveToDroneHistoryDB() {
        List<Drone> drones = Arrays.asList(createDrone(), createDrone());
        when(droneRepository.findAll()).thenReturn(drones);

        droneService.checkBatteryLevelsAndLog();

        verify(droneRepository, times(1)).findAll();
        verify(droneHistoryRepository, times(2)).save(any(DroneHistory.class));
        verifyNoMoreInteractions(droneRepository, droneHistoryRepository);
    }

    // Helper method to create a sample drone
    private Drone createDrone() {
        Drone drone = new Drone();
        drone.setId(1L);
        drone.setBatteryLevel(100);
        drone.setState(Drone.DroneState.IDLE);
        drone.setWeightLimit(50);
        drone.setLoadedMedications(new HashSet<>());
        return drone;
    }
}
