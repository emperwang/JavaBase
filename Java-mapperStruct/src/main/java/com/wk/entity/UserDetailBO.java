package com.wk.entity;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDetailBO {
    private Integer userId;
}
