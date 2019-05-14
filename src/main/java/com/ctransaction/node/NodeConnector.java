package com.ctransaction.node;

import com.alibaba.fastjson.JSONObject;
import com.ctransaction.base.utils.CommonUtils;
import com.ctransaction.base.utils.RedisLock;
import com.ctransaction.contract.CommonResult;
import com.ctransaction.contract.entity.NodeStatus;
import com.ctransaction.contract.node.CNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chpy on 19/4/6.
 */

@Component
public class NodeConnector implements IRegister {

    private final String REDIS_NODE_LOCK = "ctranscation.node.lock";
    private final String REDIS_NODE = "ctranscation.node";

    @Autowired
    ShardedJedisPool shardedJedisPool;

    public CommonResult register(CNode node) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        //redis注册
        try (ShardedJedis jedis = shardedJedisPool.getResource()) {
            //获取当前所有节点,并添加节点
            Set<String> nodeValue = jedis.smembers(REDIS_NODE);
            List<CNode> nodes = nodeValue.stream()
                    .map(t -> JSONObject.parseObject(t, CNode.class))
                    .collect(Collectors.toList());
            //取出合适的id
            int tempId = 1;
            for (CNode n : nodes) {
                //if(n.getId())
            }
            //新节点
            node.setId(tempId);
            node.setStatus(NodeStatus.HEALTH.getValue());
            //获取锁添加节点
            if (RedisLock.trtGetLock(jedis, REDIS_NODE_LOCK, 1000)) {
                result.setSuccess(true);
            }
        }
        return result;
    }

    public CommonResult unRegister(CNode node) {
        return null;
    }
}
