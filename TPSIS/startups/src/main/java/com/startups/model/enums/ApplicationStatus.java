package com.startups.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor // генерация параметризованного конструктора, кот. принимает один параметр для каждого поля
@Getter // предоставление геттера для поля
public enum ApplicationStatus {
    REGISTRATION("Оформление"),
    WAITING("Ожидание"),
    CONFIRMED("Подтверждено"),
    REJECTED("Отказано"),
    DELIVERED("Доставлено"),
    ;

    private final String name; // значение поля установлено один раз и не может быть изменено
}

