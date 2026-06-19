package main.store.Repositories;

import main.store.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c")
    List<Category> getAll();
}
