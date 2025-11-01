package com.florencee.flowerstore.models;

import org.springframework.security.core.GrantedAuthority;

public enum RoleEnum implements GrantedAuthority {
    ADMIN,      // Администратор - полный доступ ко всему
    MANAGER,    // Менеджер - управление заказами, цветами, категориями
    CUSTOMER;   // Покупатель - просмотр каталога и создание заказов

    @Override
    public String getAuthority() {
        return name();
    }
}
