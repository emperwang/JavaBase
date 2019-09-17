package com.wk.validator_custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bean {
    @MyDateValidator
    private String brithday;
}
