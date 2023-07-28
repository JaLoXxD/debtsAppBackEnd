package com.debts.debtsappbackend.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum DebtPriorityType {
    HIGH("HIGH", "debt.priority.high","#ed3636"),
    MEDIUM("MEDIUM", "debt.priority.medium", "#e5e53d"),
    LOW("LOW", "debt.priority.low", "#31ed7f");

    private final String code;
    private final String description;
    private final String color;

    public static String getDescFromCode(String code) {
        List<DebtPriorityType> itemList = Arrays.asList(DebtPriorityType.values());
        DebtPriorityType item = itemList.stream().filter(eachItem -> eachItem.getCode().equals(code)).findAny().orElse(null);
        return item != null ? item.getDescription() : "";
    }
}
