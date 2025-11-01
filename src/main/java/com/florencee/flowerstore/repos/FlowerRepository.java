package com.florencee.flowerstore.repos;

import com.florencee.flowerstore.models.Flower;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlowerRepository extends CrudRepository<Flower, Long> {
    List<Flower> findByCategoryId(Long categoryId);
}
