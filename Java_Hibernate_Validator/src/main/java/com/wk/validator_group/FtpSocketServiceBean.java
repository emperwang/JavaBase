package com.wk.validator_group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FtpSocketServiceBean {
    @NotNull(groups = {FtpService.class,SocketService.class})
    private String sourceid;

    @NotNull(groups = {FtpService.class,SocketService.class})
    @Length(max = 6,groups = {FtpService.class,SocketService.class})
    private String protocolType;

    @NotNull(groups = FtpService.class)
    @Length(max = 15,groups = FtpService.class)
    private String ftpIp;

    @NotNull(groups = FtpService.class)
    @Length(max = 5,groups = FtpService.class)
    private String ftpPort;

    @NotNull(groups = SocketService.class)
    @Length(max = 15,groups = FtpService.class)
    private String socketIp;

    @NotNull(groups = SocketService.class)
    @Length(max = 5,groups = FtpService.class)
    private String socketPort;
}
