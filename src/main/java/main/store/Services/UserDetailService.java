package main.store.Services;

import main.store.Security.CustomUserDetails;
import main.store.Entities.User;
import main.store.Repositories.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    public UserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);

        if (user == null){
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }

        return new CustomUserDetails(user,
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));

    }
}
