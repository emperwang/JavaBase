package com.wk.FTPUtil.entity;

import lombok.*;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 13:59 2020/1/3
 * @modifier:
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FtpLogin {
    private String host;
    private Integer port;
    private String user;
    private String password;
}
