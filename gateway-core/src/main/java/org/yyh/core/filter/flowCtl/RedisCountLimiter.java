package org.yyh.core.filter.flowCtl;

import lombok.extern.slf4j.Slf4j;
import org.yyh.core.redis.JedisUtil;

/**
 * @PROJECT_NAME: api-gateway
 * @DESCRIPTION: 使用Redis实现分布式限流
 * @USER: yyh
 * @DATE: 2023/4/15 23:16
 */
@Slf4j
public class RedisCountLimiter {

    protected JedisUtil jedisUtil;

    public RedisCountLimiter(JedisUtil jedisUtil) {
        this.jedisUtil = jedisUtil;
    }

    private static  final  int SUCCESS_RESULT = 1;
    private static  final  int FAILED_RESULT = 0;

    /**
     * 执行限流
      * @param key
     * @param limit
     * @param expire
     * @return
     */
    public  boolean doFlowCtl(String key,int limit,int expire){
        try {
            Object object = jedisUtil.executeScript(key,limit,expire);
            if(object == null){
                return true;
            }
            Long result = Long.valueOf(object.toString());
            if(FAILED_RESULT == result){
                return  false;
            }
        }catch (Exception e){
            throw  new RuntimeException("分布式限流发生错误");
        }
        return true;
    }




}
