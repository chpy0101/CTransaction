package com.ctransaction.contract.node;

import com.sun.org.apache.xpath.internal.operations.String;

import java.util.HashMap;

/**
 * Created by chpy on 19/3/30.
 */
public class CNode {

    private int id;
    private String ip;
    private int status;
    private HashMap<String,String> bindService;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, String> getBindService() {
        return bindService;
    }

    public void setBindService(HashMap<String, String> bindService) {
        this.bindService = bindService;
    }
}
