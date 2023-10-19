package org.yyh.core.filter.loadbalance;

import lombok.extern.slf4j.Slf4j;
import org.yyh.common.config.DynamicConfigManager;
import org.yyh.common.config.ServiceInstance;
import org.yyh.common.exception.NotFoundException;
import org.yyh.core.context.GatewayContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import static org.yyh.common.enums.ResponseCode.SERVICE_INSTANCE_NOT_FOUND;

/**
 * @PROJECT_NAME: api-gateway
 * @DESCRIPTION: 负载均衡-随机
 * @USER: yyh
 * @DATE: 2022/3/12 22:13
 */
@Slf4j
public class RandomLoadBalanceRule implements  IGatewayLoadBalanceRule{

    private final  String serviceId;

    private Set<ServiceInstance> serviceInstanceSet;

    public RandomLoadBalanceRule(String serviceId) {
        this.serviceId = serviceId;
    }

    private static ConcurrentHashMap<String,RandomLoadBalanceRule> serviceMap = new ConcurrentHashMap<>();

    public static RandomLoadBalanceRule getInstance(String serviceId){
        RandomLoadBalanceRule loadBalanceRule = serviceMap.get(serviceId);
        if(loadBalanceRule == null){
            loadBalanceRule = new RandomLoadBalanceRule(serviceId);
            serviceMap.put(serviceId,loadBalanceRule);
        }
        return loadBalanceRule;
    }


    @Override
    public ServiceInstance choose(GatewayContext ctx) {
        String serviceId = ctx.getUniqueId();
        return choose(serviceId);
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        Set<ServiceInstance> serviceInstanceSet =  DynamicConfigManager.getInstance().getServiceInstanceByUniqueId(serviceId);
        if(serviceInstanceSet.isEmpty()){
          log.warn("No instance available for:{}",serviceId);
          throw  new NotFoundException(SERVICE_INSTANCE_NOT_FOUND);
        }
        List<ServiceInstance> instances = new ArrayList<ServiceInstance>(serviceInstanceSet);
        int index = ThreadLocalRandom.current().nextInt(instances.size());
        ServiceInstance instance = (ServiceInstance)instances.get(index);
        return instance;
    }
}
