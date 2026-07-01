package main.store.Repositories;

import main.store.DTO.DTOout.CategoryOut;
import main.store.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Query("""
    SELECT new main.store.DTO.DTOout.CategoryOut(c.id, c.name)
    FROM Category c
    """)
    List<CategoryOut> getAllCategories();
}
