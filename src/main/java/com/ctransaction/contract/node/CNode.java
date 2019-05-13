package com.ctransaction.contract.node;

import com.ctransaction.contract.transaction.TransactionBlock;
import java.util.List;

/**
 * Created by chpy on 19/3/30.
 */
public class CNode {

    private int id;
    private String ip;
    private int status;
    /**
     * 节点包含的事务块
     */
    private List<TransactionBlock> blocks;

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

    public List<TransactionBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<TransactionBlock> blocks) {
        this.blocks = blocks;
    }
}
