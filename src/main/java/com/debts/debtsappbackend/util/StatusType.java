package com.debts.debtsappbackend.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum StatusType {
    ACTIVE("A", "Active"),
    INACTIVE("I", "Inactive"),
    BLOCKED("B", "Blocked");

    private final String code;
    private final String description;

    public static String getDescFromCode(String code) {
        List<StatusType> itemList = Arrays.asList(StatusType.values());
        StatusType item = itemList.stream().filter(eachItem -> eachItem.getCode().equals(code)).findAny().orElse(null);
        return item != null ? item.getDescription() : "";
    }
}
