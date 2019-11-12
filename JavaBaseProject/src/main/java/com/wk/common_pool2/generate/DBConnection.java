package com.wk.common_pool2.generate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 需要池化的对象
@Setter
@Getter
@ToString
@NoArgsConstructor
public class DBConnection {

    private boolean isActive;


    public DBConnection(boolean isActive) {
        this.isActive = isActive;
    }
}
