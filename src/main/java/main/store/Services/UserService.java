package main.store.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.store.DTO.DTOout.UserOut;
import main.store.DTO.DTOin.UserRegistration;
import main.store.Security.CustomUserDetails;
import main.store.Entities.User;
import main.store.Repositories.CartRepo;
import main.store.Repositories.UserRepo;
import main.store.Enums.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CartRepo cartRepo;


    public UserOut createUser(@Valid UserRegistration user) {
        if (userRepo.findByEmail(user.email()) != null || userRepo.findUserByName(user.name()) != null){
            throw new EntityNotFoundException("User is already exists");
        }

        User newUser = new User(user);
        newUser.setRole(UserRole.ROLE_USER);
        String hashedPassword = passwordEncoder.encode(new String(user.password()));
        newUser.setPasswordHash(hashedPassword.toCharArray());

        userRepo.save(newUser);

        return convertToUserOut(newUser);
    }

    public UserOut getCurrentUser(CustomUserDetails userDetails){
        User user = userRepo.findByEmail(userDetails.getUsername());
        return convertToUserOut(user);
    }



    private UserOut convertToUserOut(User user){
        int cartProductCount = cartRepo.findDTO(user.getId()).size();

        return new UserOut(user.getName(), user.getEmail(), cartProductCount);
    }

}
