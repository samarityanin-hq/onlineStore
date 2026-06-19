package main.store.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import main.store.DTOs.UserOut;
import main.store.DTOs.UserRegistration;
import main.store.Entities.User;
import main.store.Repositories.CartRepo;
import main.store.Repositories.UserRepo;
import main.store.Repositories.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Arrays;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CartRepo cartRepo;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, CartRepo cartRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.cartRepo = cartRepo;
    }

    public UserOut createUser(@Valid UserRegistration user) {
        if (userRepo.findByEmail(user.email()) != null || userRepo.findUserByName(user.name()) != null){
            throw new EntityNotFoundException("User is already exists");
        }

        User newUser = new User(user);
        newUser.setRole(UserRole.ROLE_USER);
        String hashedPassword = passwordEncoder.encode(Arrays.toString(user.password()));
        newUser.setPasswordHash(hashedPassword.toCharArray());

        userRepo.save(newUser);

        return convertToUserOut(newUser);
    }

    public UserOut getCurrentUser(Principal principal){
        String email = principal.getName();
        User user = userRepo.findByEmail(email);

        return convertToUserOut(user);
    }



    private UserOut convertToUserOut(User user){
        int cartProductCount = cartRepo.findByUserId(user.getId()).size();

        return new UserOut(user.getName(), user.getEmail(), cartProductCount);
    }

}
