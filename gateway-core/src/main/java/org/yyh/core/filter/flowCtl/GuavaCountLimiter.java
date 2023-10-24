package org.yyh.core.filter.flowCtl;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.yyh.common.config.Rule;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/**
 * @PROJECT_NAME: api-gateway
 * @DESCRIPTION: 单机限流
 * @USER: yyh
 * @DATE: 2023/4/15 23:17
 */
public class GuavaCountLimiter {

    private RateLimiter rateLimiter;
    private  double maxPermits;

    public GuavaCountLimiter(double maxPermits) {
        this.maxPermits = maxPermits;
        rateLimiter = RateLimiter.create(maxPermits);
    }

    public GuavaCountLimiter(double maxPermits,long warmUpPeriodAsSecond) {
        this.maxPermits = maxPermits;
        rateLimiter = RateLimiter.create(maxPermits,warmUpPeriodAsSecond, TimeUnit.SECONDS);
    }

    public static ConcurrentHashMap<String,GuavaCountLimiter> resourceRateLimiterMap = new ConcurrentHashMap<String,GuavaCountLimiter>();

    public static GuavaCountLimiter getInstance(String serviceId , Rule.FlowCtlConfig flowCtlConfig){
        if(StringUtils.isEmpty(serviceId) || flowCtlConfig ==null ||
                StringUtils.isEmpty(flowCtlConfig.getValue()) ||
                StringUtils.isEmpty(flowCtlConfig.getConfig()) ||
                StringUtils.isEmpty(flowCtlConfig.getType())){
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        String key = buffer.append(serviceId).append(".").append(flowCtlConfig.getValue()).toString();
        GuavaCountLimiter countLimiter = resourceRateLimiterMap.get(key);
        if(countLimiter == null){
            countLimiter = new GuavaCountLimiter(50);
            resourceRateLimiterMap.putIfAbsent(key,countLimiter);
        }
        return countLimiter;
    }

    public boolean acquire(int permits){
        boolean success  = rateLimiter.tryAcquire(permits);
        if(success){
            return true;
        }
      return  false;
    }
}
