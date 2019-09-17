package com.wk.validator_group;

import com.wk.ValidatorUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

public class ValidateBean {
    public static void main(String[] args) {
        Validator validator = ValidatorUtil.getValidator();

        FtpSocketServiceBean ftpSocketServiceBean = new FtpSocketServiceBean();
        ftpSocketServiceBean.setProtocolType("ftp");
        // ftpSocketServiceBean.setSourceid("001");
        ftpSocketServiceBean.setFtpIp("192.168.119.120");
        ftpSocketServiceBean.setFtpPort("65535689898989898");

        ftpSocketServiceBean.setSocketIp("192.168.119.185");
        ftpSocketServiceBean.setSocketPort("65535");

        if (ftpSocketServiceBean.getProtocolType().equals("ftp")){
            Set<ConstraintViolation<FtpSocketServiceBean>> violations = validator.validate(ftpSocketServiceBean, FtpService.class);
            ValidatorUtil.printViolations(violations);
        }
    }
}
