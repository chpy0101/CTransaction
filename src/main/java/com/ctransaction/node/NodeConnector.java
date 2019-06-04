package com.ctransaction.node;

import com.alibaba.fastjson.JSONObject;
import com.ctransaction.base.exception.LockException;
import com.ctransaction.base.utils.LockManager;
import com.ctransaction.base.utils.SortUtils;
import com.ctransaction.contract.CommonResult;
import com.ctransaction.contract.entity.NodeStatus;
import com.ctransaction.contract.node.CNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by chpy on 19/4/6.
 */

@Component
public class NodeConnector implements IRegister {

    private final String REDIS_NODE_LOCK = "ctranscation.node.lock";
    private final String REDIS_NODE = "ctranscation.node";
    private Logger logger = LoggerFactory.getLogger(NodeConnector.class);

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
            //排序
            SortUtils.insertionSort(nodes, (o1, o2) -> o1.getId() - o1.getId());
            int nodeId = getNextID(nodes);
            //新节点
            node.setId(nodeId);
            node.setStatus(NodeStatus.HEALTH.getValue());
            //获取锁添加节点
            try (LockManager.LockObject lock = LockManager.getLock(REDIS_NODE_LOCK)) {
                //获取锁成功 开始注册
                if (lock != null) {
                    result.setSuccess(true);
                    jedis.sadd(REDIS_NODE, JSONObject.toJSONString(node));//redis set集合添加
                    //通知其他节点
                    //todo。。。

                }
            }
        } catch (LockException lockEx) {
            logger.error("get redis lock error:", lockEx);
            result.setSuccess(false);
            result.setMsg("redis获取锁错误，lock key:" + REDIS_NODE_LOCK);
        } catch (Exception ex) {
            logger.error("node register error:", ex);
            result.setSuccess(false);
            result.setMsg("节点注册失败，请检查配置");
        }
        return result;
    }

    public CommonResult unRegister(CNode node) {
        return null;
    }

    //取出间断id
    private int getNextID(List<CNode> nodes) {
        int size = nodes.size(), index = 0;
        while (index <= size) {
            if (nodes.get(index).getId() != index + 1) {
                index++;
                break;
            }
            index++;
        }
        return index;
    }

}
