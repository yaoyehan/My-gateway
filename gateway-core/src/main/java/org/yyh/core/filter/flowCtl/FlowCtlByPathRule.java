package org.yyh.core.filter.flowCtl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.yyh.common.config.Rule;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.yyh.common.constants.FilterConst.*;

/**
 * @PROJECT_NAME: api-gateway
 * @DESCRIPTION: 根据路基进行流控
 * @USER: yyh
 * @DATE: 2023/4/15 22:51
 */
public class FlowCtlByPathRule implements IGatewayFlowCtlRule{

    private String serviceId;

    private String path;

    private RedisCountLimiter redisCountLimiter;

    private static final String LIMIT_MESSAGE ="您的请求过于频繁,请稍后重试";

    public FlowCtlByPathRule(String serviceId, String path,RedisCountLimiter redisCountLimiter) {
        this.serviceId = serviceId;
        this.path = path;
        this.redisCountLimiter = redisCountLimiter;
    }

    private static ConcurrentHashMap<String,FlowCtlByPathRule> servicePathMap = new ConcurrentHashMap<>();

    public static  FlowCtlByPathRule getInstance(String serviceId, String path){
        StringBuffer buffer = new StringBuffer();
        String key = buffer.append(serviceId).append(".").append(path).toString();
        FlowCtlByPathRule flowCtlByPathRule = servicePathMap.get(key);
        if(flowCtlByPathRule == null){
            flowCtlByPathRule = new FlowCtlByPathRule(serviceId,path,new RedisCountLimiter(new JedisUtil()));
            servicePathMap.put(key,flowCtlByPathRule);
        }
        return  flowCtlByPathRule;
    }

    /**
     * 根据路径执行流控
     * @param flowCtlConfig
     * @param serviceId
     */
    @Override
    public void doFlowCtlFilter(Rule.FlowCtlConfig flowCtlConfig, String serviceId) {
        if(flowCtlConfig == null || StringUtils.isEmpty(serviceId) || StringUtils.isEmpty(flowCtlConfig.getConfig())){
            return;
        }
        Map<String,Integer> configMap = JSON.parseObject(flowCtlConfig.getConfig(),Map.class);
        if(!configMap.containsKey(FLOW_CTL_LIMIT_DURATION) || !configMap.containsKey(FLOW_CTL_LIMIT_PERMITS)){
            return;
        }
        double duration = configMap.get(FLOW_CTL_LIMIT_DURATION);
        double permits = configMap.get(FLOW_CTL_LIMIT_PERMITS);
        StringBuffer buffer = new StringBuffer();
        boolean flag = true;
        String key = buffer.append(serviceId).append(".").append(path).toString();
        if(FLOW_CTL_MODEL_DISTRIBUTED.equalsIgnoreCase(flowCtlConfig.getModel())){
            flag = redisCountLimiter.doFlowCtl(key,(int)permits,(int)duration);
        }else {
            GuavaCountLimiter guavaCountLimiter = GuavaCountLimiter.getInstance(serviceId,flowCtlConfig);
            if(guavaCountLimiter == null){
                throw  new RuntimeException("获取单机限流工具类为空");
            }
            double count = Math.ceil(permits/duration);
            flag = guavaCountLimiter.acquire((int)count);
        }
        if(!flag){
            throw new RuntimeException(LIMIT_MESSAGE);
        }
    }
}
