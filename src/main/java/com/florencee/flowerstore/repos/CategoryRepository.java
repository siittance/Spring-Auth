package com.florencee.flowerstore.repos;

import com.florencee.flowerstore.models.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
