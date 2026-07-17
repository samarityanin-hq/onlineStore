package main.store.ServicesTest;

import main.store.CustomExceptions.UserAlreadyExistsException;
import main.store.DTO.DTOin.UserRegistration;
import main.store.DTO.DTOout.UserOut;
import main.store.Entities.User;
import main.store.Repositories.CartRepo;
import main.store.Repositories.UserRepo;
import main.store.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CartRepo cartRepo;

    @Mock
    private PasswordEncoder encoder;

    private UserRegistration registrationDto;

    @BeforeEach
    void setUp(){
        registrationDto = new UserRegistration(
                "user",
                "user@gmail.com",
                "secret123".toCharArray());
    }

    @Test
    void createUser_validInputData_userCreated(){
        when(userRepo.existsByEmail(registrationDto.email())).thenReturn(false);
        when(userRepo.existsByName(registrationDto.name())).thenReturn(false);
        when(encoder.encode(any(CharSequence.class))).thenReturn("hashedPassword");

        when(userRepo.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        UserOut createdUser = userService.createUser(registrationDto);

        assertNotNull(createdUser);
        verify(userRepo).existsByName(registrationDto.name());
        verify(userRepo).existsByEmail(registrationDto.email());
        verify(encoder).encode(any());
        verify(userRepo).save(any());
    }

    @Test
    void createUser_emailAlreadyExist(){
        when(userRepo.existsByEmail(registrationDto.email())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userService.createUser(registrationDto));

        verify(userRepo).existsByEmail(registrationDto.email());
        verify(userRepo, never()).existsByName(registrationDto.name());
        verify(encoder, never()).encode(any());
        verify(userRepo, never()).save(any());
    }

    @Test
    void createUser_nameAlreadyExist(){
        when(userRepo.existsByName(registrationDto.name())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(registrationDto));

        verify(userRepo).existsByEmail(registrationDto.email());
        verify(userRepo).existsByName(registrationDto.name());
        verify(encoder, never()).encode(any());
        verify(userRepo, never()).save(any());
    }


}
