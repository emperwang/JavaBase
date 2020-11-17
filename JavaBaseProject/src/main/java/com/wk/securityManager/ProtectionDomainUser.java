package com.wk.securityManager;

import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * @author: wk
 * @Date: 2020/11/17 15:53
 * @Description
 */
public class ProtectionDomainUser {

    public void domainUserInfo() throws URISyntaxException {
        ProtectionDomain domain = getClass().getProtectionDomain();
        final CodeSource codeSource = domain.getCodeSource();
        final URL location = codeSource.getLocation();
        System.out.println("-------------------domain---------------------");
        System.out.println(domain.toString());
        System.out.println("-------------------codeSource---------------------");
        System.out.println(codeSource.toString());
        System.out.println("-------------------codeLocation---------------------");
        System.out.println(location.toString());
        System.out.println("-------------------schemeSPecificPart---------------------");
        System.out.println(location.toURI().getSchemeSpecificPart());
    }

    public static void main(String[] args) throws URISyntaxException {
        final ProtectionDomainUser domainUser = new ProtectionDomainUser();
        domainUser.domainUserInfo();
    }
}
