package com.florencee.flowerstore.repos;

import com.florencee.flowerstore.models.OrderItem;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
}
