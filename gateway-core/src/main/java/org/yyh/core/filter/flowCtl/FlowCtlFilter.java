package org.yyh.core.filter.flowCtl;

import lombok.extern.slf4j.Slf4j;
import org.yyh.common.config.Rule;
import org.yyh.core.context.GatewayContext;
import org.yyh.core.filter.Filter;
import org.yyh.core.filter.FilterAspect;

import java.util.Iterator;
import java.util.Set;

import static org.yyh.common.constants.FilterConst.*;

/**
 * @PROJECT_NAME: api-gateway
 * @DESCRIPTION: 限流流控过滤器
 * @USER: yyh
 * @DATE: 2023/4/15 22:36
 */
@Slf4j
@FilterAspect(id=FLOW_CTL_FILTER_ID,
        name = FLOW_CTL_FILTER_NAME,
        order = FLOW_CTL_FILTER_ORDER)
public class FlowCtlFilter implements Filter {
    @Override
    public void doFilter(GatewayContext ctx) throws Exception {
        Rule rule = ctx.getRule();
        if(rule != null){
            Set<Rule.FlowCtlConfig>  flowCtlConfigs = rule.getFlowCtlConfigs();
            Iterator iterator = flowCtlConfigs.iterator();
            Rule.FlowCtlConfig flowCtlConfig;
            while (iterator.hasNext()){
                IGatewayFlowCtlRule flowCtlRule = null;
                flowCtlConfig = (Rule.FlowCtlConfig)iterator.next();
                if(flowCtlConfig == null){
                    continue;
                }
                String path = ctx.getRequest().getPath();
                if(flowCtlConfig.getType().equalsIgnoreCase(FLOW_CTL_TYPE_PATH)
                && path.equals(flowCtlConfig.getValue())){
                    flowCtlRule = FlowCtlByPathRule.getInstance(rule.getServiceId(),path);
                }else if(flowCtlConfig.getType().equalsIgnoreCase(FLOW_CTL_TYPE_SERVICE)){

                }
                if(flowCtlRule != null){
                    flowCtlRule.doFlowCtlFilter(flowCtlConfig,rule.getServiceId());
                }
            }
        }
    }
}
