package org.yyh.core.filter.flowCtl;

import org.yyh.common.config.Rule;

/**
 * 执行限流的接口
 */
public interface IGatewayFlowCtlRule {

    void doFlowCtlFilter(Rule.FlowCtlConfig flowCtlConfig,String serviceId);

}
