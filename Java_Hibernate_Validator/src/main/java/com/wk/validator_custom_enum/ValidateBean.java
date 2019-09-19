package com.wk.validator_custom_enum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateBean {

    @NotNull
    @EnumCheck(enumValue = {"man","women"})
    private String sex;
}
