package com.wk;

import org.eclipse.jetty.util.StringUtil;

/**
 * @author: ekiawna
 * @Date: 2021/3/12 17:17
 * @Description
 */
public class ExampleUtil {


    public static int getPort(String[] args, String property, int defaultValue){
        for (String arg : args) {
            if (arg.startsWith(property+"=")){
                String value = arg.substring(property.length()+2);
                int port = toInt(value);
                if (isValidPort(port)){
                    return port;
                }
            }
        }
        String value = System.getProperty(property);
        int port = toInt(value);
        if (isValidPort(port)){
            return port;
        }
        return defaultValue;
    }


    private static boolean isValidPort(int port){
        return (port > 0) && (port < 65535);
    }


    private static int toInt(String value){
        if (StringUtil.isBlank(value)){
            return -1;
        }

        try {
            return Integer.parseInt(value);
        }catch (NumberFormatException e){
            return -1;
        }
    }
}
