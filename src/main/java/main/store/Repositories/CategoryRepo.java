package main.store.Repositories;

import main.store.DTOs.CategoryOut;
import main.store.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Query("""
    SELECT new main.store.DTOs.CategoryOut(c.id, c.name)
    FROM Category c
    """)
    List<CategoryOut> getAllCategories();
}
