package com.ctransaction.contract.transaction;

import com.ctransaction.base.contant.TransactionBlockType;

/**
 * Created by chpy on 19/5/5.
 */
public class TransactionBlock {

    private String key;
    /*
    事务模块类型(回调等。。。)
     */
    private TransactionBlockType type;
    /*
    执行顺序
     */
    private int sort;
    /*
    参数构造器(构造下一个事务块请求参数)
    */
    private ITransactionParamMapper mapper;

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TransactionBlockType getType() {
        return type;
    }

    public void setType(TransactionBlockType type) {
        this.type = type;
    }

    public ITransactionParamMapper getMapper() {
        return mapper;
    }

    public void setMapper(ITransactionParamMapper mapper) {
        this.mapper = mapper;
    }
}
