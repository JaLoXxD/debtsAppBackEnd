package com.debts.debtsappbackend.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DebtCategoryType {
    LOAN("LOAN", "debt.category.loan"),
    FOOD("FOOD", "debt.category.food"),
    ENTERTAINMENT("ENTERTAINMENT", "debt.category.entertainment"),
    TRANSPORT("TRANSPORT", "debt.category.transport"),
    OTHER("OTHER", "debt.category.other");

    private final String code;
    private final String description;
}
