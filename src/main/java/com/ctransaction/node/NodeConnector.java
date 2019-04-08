package com.ctransaction.node;

import com.ctransaction.contract.CommonResult;
import com.ctransaction.contract.node.CNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by chpy on 19/4/6.
 */

@Component
public class NodeConnector implements IRegister {

    @Autowired
    ShardedJedisPool shardedJedisPool;

    public CommonResult register(CNode node) {
        return null;
    }

    public CommonResult unRegister(CNode node) {
        return null;
    }
}
