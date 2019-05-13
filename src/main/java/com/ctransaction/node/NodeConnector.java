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
        try(ShardedJedis jedis = shardedJedisPool.getResource()) {
            //获取当前所有节点,并添加节点
            Set<String> nodeValue = jedis.smembers(REDIS_NODE);
            List<CNode> nodes = nodeValue.stream()
                    .map(t -> JSONObject.parseObject(t, CNode.class))
                    .collect(Collectors.toList());
            //取出最后一个节点
            Optional<CNode> lastNode = nodes.stream().max(Comparator.comparingInt(CNode::getId));
            Integer maxId = lastNode.isPresent() ? lastNode.get().getId() : 0;
            //新节点
            //获取锁添加节点
            if(RedisLock.trtGetLock(jedis,REDIS_NODE_LOCK,1000)){
                result.setSuccess(true);
            }
        }
        return result;
    }

    public CNode createNode(Integer maxId){
        CNode node = new CNode();
        node.setId(maxId);
        node.setIp(CommonUtils.getLocalIP());
        node.setStatus(NodeStatus.HEALTH.getValue());
        //todo...
        return node;
    }

    public CommonResult unRegister(CNode node) {
        return null;
    }
}
