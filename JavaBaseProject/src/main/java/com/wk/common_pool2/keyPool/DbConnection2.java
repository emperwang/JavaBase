package com.wk.common_pool2.keyPool;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 要池化的对象
@Setter
@Getter
@ToString
@NoArgsConstructor
public class DbConnection2 {
    private boolean isActive;

    private String url;

    public DbConnection2( String url) {
        this.url = url;
    }
}
