package com.wk.openldap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Properties;

public class LdapService {


    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL,"ldap://192.168.30.15:389");
        properties.put(Context.SECURITY_AUTHENTICATION,"simple");
        properties.put(Context.SECURITY_PRINCIPAL,"cn=admin,dc=example,dc=com");
        properties.put(Context.SECURITY_CREDENTIALS, "loongson");


        try {
            InitialDirContext dirContext = new InitialDirContext(properties);
            System.out.println("ldap connect success");

            String searchBase = "dc=example,dc=com";
            String filter = "(objectClass=person)";
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> results = dirContext.search(searchBase, filter, controls);

            while (results.hasMore()){
                SearchResult result = results.next();
                System.out.println(result.getNameInNamespace());
                System.out.println("name:= " + result.getName());
                System.out.println("className= " + result.getClassName());
                NamingEnumeration<? extends Attribute> attris = result.getAttributes().getAll();
                while(attris.hasMore()) {
                    Attribute attribute = attris.nextElement();
                    System.out.println(attribute.toString());

                }
            }

        } catch (NamingException e) {
            e.printStackTrace();
        }

    }

}
