package com.investment.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProjectAppStatus {
    WAITING("Ожидание"),
    DONE("Подтверждено"),
    REJECT("Отклонено"),
    ;

    private final String name;

}

