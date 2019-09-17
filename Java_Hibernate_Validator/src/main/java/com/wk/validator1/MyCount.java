package com.wk.validator1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *  需要进行校验的bean
 */
// lombok 使用
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCount {
    // 此field 不能为nul
    @NotNull
    private String id;
    // 此field不能为null,并且长度最大为10
    @NotNull
    @Length(max=10)
    private String userName;
    // 不能为null,并且必须得符合此正则表达式
    @NotNull
    @Pattern(regexp = "[a-z][A-Z]")
    private String password;
    // 指定此 field 的最大 最小值范围
    @Max(10)
    @Min(1)
    private Integer level;
}
