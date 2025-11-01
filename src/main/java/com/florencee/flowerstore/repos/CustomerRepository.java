package com.florencee.flowerstore.repos;

import com.florencee.flowerstore.models.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Customer findByUsername(String username);
    boolean existsByUsername(String username);
}
