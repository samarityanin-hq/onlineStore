package main.store.ControllersTest;

import main.store.Controllers.RegistrationController;
import main.store.CustomExceptions.UserAlreadyExistsException;
import main.store.DTO.DTOin.UserRegistration;
import main.store.DTO.DTOout.UserOut;
import main.store.Services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = RegistrationController.class)
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private org.springframework.cache.CacheManager cacheManager;


    @Test
    void createUser_validInputData_userCreated() throws Exception {
        String json = """
                {
                "name":"name",
                "email":"name@email.en",
                "password":"123qwetq455"
                }
                """;

        UserOut createdUser = new UserOut(
                "name",
                "name@email.en",
                0);
        when(userService.createUser(any(UserRegistration.class))).thenReturn(createdUser);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.email").value("name@email.en"))
                .andExpect(jsonPath("$.cartItems").value(0));
    }

    @Test
    void createUser_validInput_userAlreadyExists() throws Exception {
        String json = """
                {
                "name":"name",
                "email":"name@email.en",
                "password":"123qwetq455"
                }
                """;

        when(userService.createUser(any(UserRegistration.class)))
                .thenThrow(new UserAlreadyExistsException("email", "name@email.en"));

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("User with email: name@email.en already exists"));

    }

    @Test
    void createUser_invalidInput_emptyNameField() throws Exception {
        String json = """
                {
                "name":"xx",
                "email":"name@email.en",
                "password":"123qwetq455"
                }
                """;

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("name: name should be min 3 symbols"));

    }


}


