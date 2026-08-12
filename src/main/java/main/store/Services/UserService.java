package main.store.Services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.store.Config.JwtService;
import main.store.DTO.Request.UserCredentials;
import main.store.DTO.Response.JwtAuthentication;
import main.store.Exceptions.CustomExceptions.InvalidTokenException;
import main.store.Exceptions.CustomExceptions.UserAlreadyExistsException;
import main.store.DTO.Response.UserOut;
import main.store.DTO.Request.UserRegistration;
import main.store.Security.CustomUserDetails;
import main.store.Entities.User;
import main.store.Repositories.CartRepo;
import main.store.Repositories.UserRepo;
import main.store.Enums.UserRole;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CartRepo cartRepo;


    public UserOut createUser(@Valid UserRegistration user) {
        if (userRepo.existsByEmail(user.email())){
            throw new UserAlreadyExistsException("email", user.email());
        }

        if (userRepo.existsByName(user.name())){
            throw new UserAlreadyExistsException("name", user.name());
        }

        User newUser = new User(user);
        newUser.setRole(UserRole.ROLE_USER);
        String hashedPassword = passwordEncoder.encode(new String(user.password()));
        newUser.setPasswordHash(hashedPassword.toCharArray());
        userRepo.save(newUser);

        return convertToUserOut(newUser);
    }

    public UserOut getCurrentUser(CustomUserDetails userDetails){
        User user = userRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + userDetails.getUsername() + " not found"));
        return convertToUserOut(user);
    }


    private UserOut convertToUserOut(User user){
        int cartProductCount = cartRepo.findDTO(user.getId()).size();

        return new UserOut(user.getName(), user.getEmail(), cartProductCount);
    }

    public JwtAuthentication login(@Valid UserCredentials credentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.email(),
                        credentials.password()
                )
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtService.generateAuthToken(userDetails.getUser());

    }



    public void logout(String refreshToken) {
        jwtService.logout(refreshToken);
    }

    public JwtAuthentication refreshToken(String oldRefreshToken) {
        String email = jwtService.getEmailFromToken(oldRefreshToken);
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));

        if (!jwtService.isRefreshToken(oldRefreshToken) || !jwtService.validateJwtToken(oldRefreshToken)){
            throw new InvalidTokenException("Refresh token expired or already used");
        }

        return jwtService.refreshAuthToken(user, oldRefreshToken);
    }
}
