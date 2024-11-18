package ar.gym.gym.service.impl;

import ar.gym.gym.controller.ClientController;
import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.request.ClientStatusRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;

import ar.gym.gym.dto.response.ClientStatusResponseDto;
import ar.gym.gym.mapper.ClientMapper;
import ar.gym.gym.mapper.ClientStatusMapper;
import ar.gym.gym.mapper.GymMapper;
import ar.gym.gym.mapper.NotificationMapper;
import ar.gym.gym.model.*;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.repository.ClientStatusRepository;
import ar.gym.gym.repository.GymRepository;
import ar.gym.gym.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(ClientServiceImpl.class)
public class ClientServiceImplTest {

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private GymRepository gymRepository;

    @MockBean
    private ClientMapper clientMapper;

    @MockBean
    private GymMapper gymMapper;

    @MockBean
    private ClientStatusRepository clientStatusRepository;

    @MockBean
    private ClientStatusMapper clientStatusMapper;

    @MockBean
    private NotificationMapper notificationMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        clientService = new ClientServiceImpl(clientRepository, gymRepository, clientMapper, gymMapper, clientStatusRepository, clientStatusMapper, notificationMapper);
    }

    @Test
    public void testCreateClientWithValidData() throws Exception {
        // Arrange
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setName("John");
        clientRequestDto.setLastname("Doe");
        clientRequestDto.setDni("12345678");
        clientRequestDto.setPhone("1234567890");
        clientRequestDto.setAddress("123 Main St");
        clientRequestDto.setEmail("john.doe@example.com");
        clientRequestDto.setActive(true);
        clientRequestDto.setGoal("Lose weight");
        clientRequestDto.setInitialPhysicalState("Good");

        ClientResponseDto clientResponseDto = new ClientResponseDto();
        clientResponseDto.setName("John");
        clientResponseDto.setLastname("Doe");
        clientResponseDto.setDni("12345678");
        clientResponseDto.setPhone("1234567890");
        clientResponseDto.setAddress("123 Main St");
        clientResponseDto.setEmail("john.doe@example.com");
        clientResponseDto.setActive(true);
        clientResponseDto.setGoal("Lose weight");
        clientResponseDto.setInitialPhysicalState("Good");

        when(clientRepository.findByDni(clientRequestDto.getDni())).thenReturn(Optional.empty());
        when(clientRepository.findByPhone(clientRequestDto.getPhone())).thenReturn(Optional.empty());
        when(clientRepository.findByEmail(clientRequestDto.getEmail())).thenReturn(Optional.empty());
        when(clientMapper.dtoToEntity(clientRequestDto)).thenReturn(new Client());
        when(clientRepository.save(any(Client.class))).thenReturn(new Client());
        when(clientMapper.entityToDto(any(Client.class))).thenReturn(clientResponseDto);

        // Act & Assert
        ClientResponseDto result = clientService.create(clientRequestDto);
        assertEquals(clientResponseDto, result);
    }

    @Test
    public void testCreateClientWithDuplicateDni() {
        // Arrange
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setDni("12345678");

        when(clientRepository.findByDni(clientRequestDto.getDni())).thenReturn(Optional.of(new Client()));

        // Act & Assert
        assertThrows(EntityExistsException.class, () -> clientService.create(clientRequestDto));
    }

    @Test
    public void testCreateClientWithDuplicatePhone() {
        // Arrange
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setPhone("1234567890");

        when(clientRepository.findByPhone(clientRequestDto.getPhone())).thenReturn(Optional.of(new Client()));

        // Act & Assert
        assertThrows(EntityExistsException.class, () -> clientService.create(clientRequestDto));
    }

    @Test
    public void testCreateClientWithDuplicateEmail() {
        // Arrange
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setEmail("john.doe@example.com");

        when(clientRepository.findByEmail(clientRequestDto.getEmail())).thenReturn(Optional.of(new Client()));

        // Act & Assert
        assertThrows(EntityExistsException.class, () -> clientService.create(clientRequestDto));
    }

    @Test
    public void testCreateClientWithGymNotFound() {
        // Arrange
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setGymName("NonExistentGym");

        when(gymRepository.findByName(clientRequestDto.getGymName())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> clientService.create(clientRequestDto));
    }

    @Test
    public void testFindAll_NoClients() {
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());

        List<ClientResponseDto> result = clientService.findAll();

        assertTrue(result.isEmpty());
        verify(clientRepository, times(1)).findAll();
        verify(clientMapper, never()).entityToDto(any(Client.class));
    }

    @Test
    public void testFindAll_SingleClient() {
        Client client = new Client();
        ClientResponseDto clientDto = new ClientResponseDto();

        when(clientRepository.findAll()).thenReturn(Collections.singletonList(client));
        when(clientMapper.entityToDto(client)).thenReturn(clientDto);

        List<ClientResponseDto> result = clientService.findAll();

        assertEquals(1, result.size());
        assertEquals(clientDto, result.get(0));
        verify(clientRepository, times(1)).findAll();
        verify(clientMapper, times(1)).entityToDto(client);
    }

    @Test
    public void testFindAll_MultipleClients() {
        Client client1 = new Client();
        Client client2 = new Client();
        ClientResponseDto clientDto1 = new ClientResponseDto();
        ClientResponseDto clientDto2 = new ClientResponseDto();

        when(clientRepository.findAll()).thenReturn(Arrays.asList(client1, client2));
        when(clientMapper.entityToDto(client1)).thenReturn(clientDto1);
        when(clientMapper.entityToDto(client2)).thenReturn(clientDto2);

        List<ClientResponseDto> result = clientService.findAll();

        assertEquals(2, result.size());
        assertEquals(clientDto1, result.get(0));
        assertEquals(clientDto2, result.get(1));
        verify(clientRepository, times(1)).findAll();
        verify(clientMapper, times(2)).entityToDto(any(Client.class)); // Ajusta la verificación para dos llamadas
    }

    @Test
    public void testGetClientByDniOrThrow_ClientExists() {
        String dni = "12345678";
        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");

        when(clientRepository.findByDni(dni)).thenReturn(Optional.of(client));

        Client result = clientService.getClientByDniOrThrow(dni);

        assertEquals(client, result);
        verify(clientRepository, times(1)).findByDni(dni);
    }

    @Test
    public void testGetClientByDniOrThrow_ClientDoesNotExist() {
        String dni = "12345678";

        when(clientRepository.findByDni(dni)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.getClientByDniOrThrow(dni);
        });

        assertEquals("El cliente con el dni " + dni + " no existe", exception.getMessage());
        verify(clientRepository, times(1)).findByDni(dni);
    }

    @Test
    public void testFindByDni_ClientExists() {
        String dni = "12345678";
        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        ClientResponseDto clientDto = new ClientResponseDto();

        when(clientRepository.findByDni(dni)).thenReturn(Optional.of(client));
        when(clientMapper.entityToDto(client)).thenReturn(clientDto);

        ClientResponseDto result = clientService.findByDni(dni);

        assertEquals(clientDto, result);
        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientMapper, times(1)).entityToDto(client);
    }

    @Test
    public void testFindByDni_ClientDoesNotExist() {
        String dni = "12345678";

        when(clientRepository.findByDni(dni)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.findByDni(dni);
        });

        assertEquals("El cliente con el dni " + dni + " no existe", exception.getMessage());
        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientMapper, never()).entityToDto(any(Client.class));
    }

    @Test
    public void testFindByDni_WithGymAndNutritionistAndTrainer() {
        String dni = "12345678";
        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        Gym gym = new Gym();
        gym.setName("Gym Name");
        client.setGym(gym);
        Nutritionist nutritionist = new Nutritionist();
        nutritionist.setDni("nutritionistDni");
        client.setNutritionist(nutritionist);
        Trainer trainer = new Trainer();
        trainer.setDni("trainerDni");
        client.setTrainer(trainer);

        ClientResponseDto clientDto = new ClientResponseDto();

        when(clientRepository.findByDni(dni)).thenReturn(Optional.of(client));
        when(clientMapper.entityToDto(client)).thenReturn(clientDto);

        ClientResponseDto result = clientService.findByDni(dni);

        assertEquals(clientDto, result);
        assertEquals("Gym Name", result.getGymName());
        assertEquals("nutritionistDni", result.getNutritionistDni());
        assertEquals("trainerDni", result.getTrainerDni());
        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientMapper, times(1)).entityToDto(client);
    }


    @Test
    public void testFindByEmail_ClientExists() {
        String email = "test@example.com";
        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        ClientResponseDto clientDto = new ClientResponseDto();

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(clientMapper.entityToDto(client)).thenReturn(clientDto);

        ClientResponseDto result = clientService.findByEmail(email);

        assertEquals(clientDto, result);
        verify(clientRepository, times(1)).findByEmail(email);
        verify(clientMapper, times(1)).entityToDto(client);
    }

    @Test
    public void testFindByEmail_ClientDoesNotExist() {
        String email = "test@example.com";

        when(clientRepository.findByEmail(email)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.findByEmail(email);
        });

        assertEquals("El cliente con el email " + email + " no existe", exception.getMessage());
        verify(clientRepository, times(1)).findByEmail(email);
        verify(clientMapper, never()).entityToDto(any(Client.class));
    }

    @Test
    public void testFindByEmail_WithGymAndNutritionistAndTrainer() {
        String email = "test@example.com";
        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        Gym gym = new Gym();
        gym.setName("Gym Name");
        client.setGym(gym);
        Nutritionist nutritionist = new Nutritionist();
        nutritionist.setDni("nutritionistDni");
        client.setNutritionist(nutritionist);
        Trainer trainer = new Trainer();
        trainer.setDni("trainerDni");
        client.setTrainer(trainer);

        ClientResponseDto clientDto = new ClientResponseDto();

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(clientMapper.entityToDto(client)).thenReturn(clientDto);

        ClientResponseDto result = clientService.findByEmail(email);

        assertEquals(clientDto, result);
        assertEquals("Gym Name", result.getGymName());
        assertEquals("nutritionistDni", result.getNutritionistDni());
        assertEquals("trainerDni", result.getTrainerDni());
        verify(clientRepository, times(1)).findByEmail(email);
        verify(clientMapper, times(1)).entityToDto(client);
    }

    @Test
    public void testUpdate_ClientExists() {
        Long id = 1L;
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setName("Updated Name");
        clientRequestDto.setLastname("Updated Lastname");
        clientRequestDto.setDni("Updated DNI");
        clientRequestDto.setPhone("Updated Phone");
        clientRequestDto.setAddress("Updated Address");
        clientRequestDto.setEmail("updated@example.com");
        clientRequestDto.setActive(true);
        clientRequestDto.setGoal("Updated Goal");

        Client existingClient = new Client();
        existingClient.setId(id);

        Client updatedClient = new Client();
        updatedClient.setId(id);
        updatedClient.setName("Updated Name");
        updatedClient.setLastname("Updated Lastname");
        updatedClient.setDni("Updated DNI");
        updatedClient.setPhone("Updated Phone");
        updatedClient.setAddress("Updated Address");
        updatedClient.setEmail("updated@example.com");
        updatedClient.setActive(true);
        updatedClient.setGoal("Updated Goal");

        ClientResponseDto clientDto = new ClientResponseDto();

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);
        when(clientMapper.entityToDto(updatedClient)).thenReturn(clientDto);

        ClientResponseDto result = clientService.update(clientRequestDto, id);

        assertEquals(clientDto, result);
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(clientMapper, times(1)).entityToDto(updatedClient);
    }

    @Test
    public void testUpdate_ClientDoesNotExist() {
        Long id = 1L;
        ClientRequestDto clientRequestDto = new ClientRequestDto();

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.update(clientRequestDto, id);
        });

        assertEquals("Cliente no encontrado con el ID: " + id, exception.getMessage());
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, never()).save(any(Client.class));
        verify(clientMapper, never()).entityToDto(any(Client.class));
    }

    @Test
    public void testUpdate_GymDoesNotExist() {
        Long id = 1L;
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setGymName("NonExistent Gym");

        Client existingClient = new Client();
        existingClient.setId(id);

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(gymRepository.findByName("NonExistent Gym")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.update(clientRequestDto, id);
        });

        assertEquals("Gimnasio no encontrado con el nombre: NonExistent Gym", exception.getMessage());
        verify(clientRepository, times(1)).findById(id);
        verify(gymRepository, times(1)).findByName("NonExistent Gym");
        verify(clientRepository, never()).save(any(Client.class));
        verify(clientMapper, never()).entityToDto(any(Client.class));
    }

    @Test
    public void testDelete_ClientExists() {
        String dni = "12345678";
        Client client = new Client();
        client.setId(1L);
        client.setDni(dni);

        when(clientRepository.findByDni(dni)).thenReturn(Optional.of(client));

        clientService.delete(dni);

        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    public void testDelete_ClientDoesNotExist() {
        String dni = "12345678";

        when(clientRepository.findByDni(dni)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.delete(dni);
        });

        assertEquals("El cliente con el dni " + dni + " no existe", exception.getMessage());
        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientRepository, never()).delete(any(Client.class));
    }

    @Test
    public void testDisableClientByDni_ClientExists() {
        String dni = "12345678";
        Client client = new Client();
        client.setId(1L);
        client.setDni(dni);
        client.setActive(true);

        ClientResponseDto clientDto = new ClientResponseDto();

        when(clientRepository.findByDni(dni)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.entityToDto(client)).thenReturn(clientDto);

        ClientResponseDto result = clientService.disableClientByDni(dni);

        assertEquals(clientDto, result);
        assertFalse(client.isActive());
        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientRepository, times(1)).save(client);
        verify(clientMapper, times(1)).entityToDto(client);
    }

    @Test
    public void testDisableClientByDni_ClientDoesNotExist() {
        String dni = "12345678";

        when(clientRepository.findByDni(dni)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.disableClientByDni(dni);
        });

        assertEquals("Cliente no encontrado con DNI: " + dni, exception.getMessage());
        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientRepository, never()).save(any(Client.class));
        verify(clientMapper, never()).entityToDto(any(Client.class));
    }

    @Test
    public void testFindClientStatusesByDni_ClientExistsWithStatuses() {
        String dni = "12345678";
        Client client = new Client();
        client.setId(1L);
        client.setDni(dni);
        ClientStatus status1 = new ClientStatus();
        ClientStatus status2 = new ClientStatus();
        client.setStatuses(Arrays.asList(status1, status2));

        ClientStatusResponseDto statusDto1 = new ClientStatusResponseDto();
        ClientStatusResponseDto statusDto2 = new ClientStatusResponseDto();

        when(clientRepository.findByDni(dni)).thenReturn(Optional.of(client));
        when(clientStatusMapper.entityToDto(status1)).thenReturn(statusDto1);
        when(clientStatusMapper.entityToDto(status2)).thenReturn(statusDto2);

        List<ClientStatusResponseDto> result = clientService.findClientStatusesByDni(dni);

        assertEquals(2, result.size());
        assertTrue(result.contains(statusDto1));
        assertTrue(result.contains(statusDto2));
        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientStatusMapper, times(1)).entityToDto(status1);
        verify(clientStatusMapper, times(1)).entityToDto(status2);
    }

    @Test
    public void testFindClientStatusesByDni_ClientDoesNotExist() {
        String dni = "12345678";

        when(clientRepository.findByDni(dni)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.findClientStatusesByDni(dni);
        });

        assertEquals("El cliente con el dni " + dni + " no existe", exception.getMessage());
        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientStatusMapper, never()).entityToDto(any(ClientStatus.class));
    }

    @Test
    public void testFindClientStatusesByDni_ClientExistsWithoutStatuses() {
        String dni = "12345678";
        Client client = new Client();
        client.setId(1L);
        client.setDni(dni);
        client.setStatuses(Collections.emptyList());

        when(clientRepository.findByDni(dni)).thenReturn(Optional.of(client));

        List<ClientStatusResponseDto> result = clientService.findClientStatusesByDni(dni);

        assertTrue(result.isEmpty());
        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientStatusMapper, never()).entityToDto(any(ClientStatus.class));
    }

    @Test
    public void testAddClientStatus_ClientExists() {
        String dni = "12345678";
        Client client = new Client();
        client.setId(1L);
        client.setDni(dni);
        client.setStatuses(new ArrayList<>());

        ClientStatusRequestDto newStatusRequestDto = new ClientStatusRequestDto();
        newStatusRequestDto.setWeight(70.0);
        newStatusRequestDto.setHeight(170.0);
        newStatusRequestDto.setBodymass(24.0);
        newStatusRequestDto.setBodyfat(20.0);

        ClientStatus newStatus = new ClientStatus();
        newStatus.setCreationDate(LocalDate.now());

        ClientStatusResponseDto newStatusResponseDto = new ClientStatusResponseDto();

        when(clientRepository.findByDni(dni)).thenReturn(Optional.of(client));
        when(clientStatusMapper.dtoToEntity(newStatusRequestDto)).thenReturn(newStatus);
        when(clientStatusRepository.save(newStatus)).thenReturn(newStatus);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientStatusMapper.entityToDto(newStatus)).thenReturn(newStatusResponseDto);

        ClientStatusResponseDto result = clientService.addClientStatus(dni, newStatusRequestDto);

        assertEquals(newStatusResponseDto, result);
        assertEquals(1, client.getStatuses().size());
        assertTrue(client.getStatuses().contains(newStatus));
        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientStatusMapper, times(1)).dtoToEntity(newStatusRequestDto);
        verify(clientStatusRepository, times(1)).save(newStatus);
        verify(clientRepository, times(1)).save(client);
        verify(clientStatusMapper, times(1)).entityToDto(newStatus);
    }

    @Test
    public void testAddClientStatus_ClientDoesNotExist() {
        String dni = "12345678";
        ClientStatusRequestDto newStatusRequestDto = new ClientStatusRequestDto();

        when(clientRepository.findByDni(dni)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.addClientStatus(dni, newStatusRequestDto);
        });

        assertEquals("Cliente no encontrado con el dni: " + dni, exception.getMessage());
        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientStatusMapper, never()).dtoToEntity(any(ClientStatusRequestDto.class));
        verify(clientStatusRepository, never()).save(any(ClientStatus.class));
        verify(clientRepository, never()).save(any(Client.class));
        verify(clientStatusMapper, never()).entityToDto(any(ClientStatus.class));
    }

    @Test
    public void testAddClientStatus_InvalidStatusData() {
        String dni = "12345678";
        Client client = new Client();
        client.setId(1L);
        client.setDni(dni);
        client.setStatuses(new ArrayList<>());

        ClientStatusRequestDto newStatusRequestDto = new ClientStatusRequestDto();
        newStatusRequestDto.setWeight(-10.0); // Invalid weight

        when(clientRepository.findByDni(dni)).thenReturn(Optional.of(client));

        assertThrows(ConstraintViolationException.class, () -> {
            clientService.addClientStatus(dni, newStatusRequestDto);
        });

        verify(clientRepository, times(1)).findByDni(dni);
        verify(clientStatusMapper, never()).dtoToEntity(any(ClientStatusRequestDto.class));
        verify(clientStatusRepository, never()).save(any(ClientStatus.class));
        verify(clientRepository, never()).save(any(Client.class));
        verify(clientStatusMapper, never()).entityToDto(any(ClientStatus.class));
    }



}
