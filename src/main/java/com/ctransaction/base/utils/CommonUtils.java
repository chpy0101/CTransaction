package com.ctransaction.base.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by chpy on 19/5/13.
 */
public class CommonUtils {
    public static String getLocalIP(){
        try {
            return InetAddress.getLocalHost().getHostAddress().toString();
        } catch (UnknownHostException e) {
            return "0.0.0.0";
        }
    }
}
