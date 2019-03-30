package com.ctransaction.node;

import com.ctransaction.contract.CommonResult;
import com.ctransaction.contract.node.CNode;

/**
 * Created by chpy on 19/3/30.
 */
public interface IRegister {

    CommonResult register(CNode node);

    CommonResult unRegister(CNode node);
}
