package main.store.Repositories;

import main.store.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findUserByName(String name);
    User findByEmail(String email);


}
