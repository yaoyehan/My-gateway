package org.yyh.common.constants;

/**
 * 负载均衡常量类
 */
public interface FilterConst {

    String USER_AUTH_FILTER_ID = "user_auth_filter";
    String USER_AUTH_FILTER_NAME = "user_auth_filter";
    int USER_AUTH_FILTER_ORDER = 1;

    String LOAD_BALANCE_FILTER_ID = "load_balance_filter";
    String LOAD_BALANCE_FILTER_NAME = "load_balance_filter";
    int LOAD_BALANCE_FILTER_ORDER = 100;

    String LOAD_BALANCE_KEY = "load_balance";
    String LOAD_BALANCE_STRATEGY_RANDOM = "Random";
    String LOAD_BALANCE_STRATEGY_ROUND_ROBIN = "RoundRobin";


    String ROUTER_FILTER_ID = "router_filter";
    String ROUTER_FILTER_NAME = "router_filter";
    int ROUTER_FILTER_ORDER = Integer.MAX_VALUE;


}
