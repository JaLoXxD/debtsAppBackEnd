package com.debts.debtsappbackend.helper;

import com.debts.debtsappbackend.dto.DebtCategoryDto;
import com.debts.debtsappbackend.dto.UserDto;
import com.debts.debtsappbackend.entity.DebtCategory;
import com.debts.debtsappbackend.model.request.CreateDebtCategoryRequest;
import com.debts.debtsappbackend.model.response.CreateDebtCategoryResponse;
import com.debts.debtsappbackend.model.response.DefaultDebtCategoriesResponse;
import com.debts.debtsappbackend.model.response.GetAllDebtsCategoriesResponse;
import com.debts.debtsappbackend.services.TranslateService;
import com.debts.debtsappbackend.util.DebtCategoryType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DebtCategoryHelper {
    private final TranslateService translateService;
    private final UserHelper userHelper;

    public DebtCategoryHelper(TranslateService translateService, UserHelper userHelper) {
        this.translateService = translateService;
        this.userHelper = userHelper;
    }

    public CreateDebtCategoryResponse buildCreateDebtCategoryResponse(DebtCategory debtCategory, List<String> errors){
        try {
            if(!errors.isEmpty()){
                return CreateDebtCategoryResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.category.create.error"))
                        .errors(errors)
                        .build();
            }
            return CreateDebtCategoryResponse.builder()
                    .success(true)
                    .message(translateService.getMessage("debt.category.create.success"))
                    .debtCategory(convertDebtCategoryToDto(debtCategory, true))
                    .build();
        } catch(Exception e){
            return CreateDebtCategoryResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.category.create.error"))
                    .errors(errors)
                    .build();
        }
    }

    public DebtCategoryDto convertDebtCategoryToDto(DebtCategory debtCategory, Boolean withUser){
        UserDto userDto = debtCategory.getUser() != null ? userHelper.convertToDto(debtCategory.getUser()) : null;
        return DebtCategoryDto.builder()
                .id(debtCategory.getId())
                .name(debtCategory.getName())
                .description(translateService.getMessage(debtCategory.getDescription()))
                .global(debtCategory.getGlobal())
                .user(withUser ? userDto : null)
                .build();
    }

    public DebtCategory mapDebtCategoryFromRequest(CreateDebtCategoryRequest request){
        return DebtCategory.builder()
                .name(request.getName().toUpperCase())
                .description(request.getDescription())
                .global(false)
                .build();
    }

    public DebtCategory convertDebtCategoryTypeToDebtCategory(DebtCategoryType debtCategoryType){
        return DebtCategory.builder()
                .name(debtCategoryType.getCode())
                .description(debtCategoryType.getDescription())
                .global(true)
                .build();
    }

    public DefaultDebtCategoriesResponse buildDefaultDebtCategoriesResponse(List<DebtCategory> debtCategories, List<String> errors) {
        try{
            if(!errors.isEmpty()){
                return DefaultDebtCategoriesResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.create.defaultCategories.error"))
                        .errors(errors)
                        .build();
            }
            return DefaultDebtCategoriesResponse.builder()
                    .success(true)
                    .message(translateService.getMessage("debt.create.defaultCategories.success"))
                    .debtCategories(debtCategories.stream().map(debtCategory -> convertDebtCategoryToDto(debtCategory, false)).collect(Collectors.toList()))
                    .build();
        } catch(Exception e){
            return DefaultDebtCategoriesResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.create.defaultCategories.error"))
                    .errors(errors)
                    .build();
        }
    }

    public GetAllDebtsCategoriesResponse buildGetAllDebtsCategoriesResponse(List<DebtCategory> userDebtCategories, List<DebtCategory> globalDebtCategories, List<String> errors) {
        try{
            if(!errors.isEmpty()){
                return GetAllDebtsCategoriesResponse.builder()
                        .success(false)
                        .message(translateService.getMessage("debt.category.get.all.error"))
                        .errors(errors)
                        .build();
            }
            List<DebtCategoryDto> userDebtCategoriesDto = userDebtCategories.stream().map(debtCategory -> convertDebtCategoryToDto(debtCategory, false)).collect(Collectors.toList());
            List<DebtCategoryDto> globalDebtCategoriesDto = globalDebtCategories.stream().map(debtCategory -> convertDebtCategoryToDto(debtCategory, false)).collect(Collectors.toList());
            List<DebtCategoryDto> allDebtCategoriesDto = new ArrayList<>();
            allDebtCategoriesDto.addAll(userDebtCategoriesDto);
            allDebtCategoriesDto.addAll(globalDebtCategoriesDto);
            return GetAllDebtsCategoriesResponse.builder()
                    .success(true)
                    .message(translateService.getMessage("debt.category.get.all.success"))
                    .userDebtCategories(userDebtCategoriesDto)
                    .globalDebtCategories(globalDebtCategoriesDto)
                    .allDebtCategories(allDebtCategoriesDto)
                    .build();
        } catch(Exception e) {
            return GetAllDebtsCategoriesResponse.builder()
                    .success(false)
                    .message(translateService.getMessage("debt.category.get.all.error"))
                    .errors(errors)
                    .build();
        }
    }
}
