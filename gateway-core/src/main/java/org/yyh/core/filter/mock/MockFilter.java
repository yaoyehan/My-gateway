package org.yyh.core.filter.mock;

import lombok.extern.slf4j.Slf4j;
import org.yyh.common.config.Rule;
import org.yyh.common.utils.JSONUtil;
import org.yyh.core.context.GatewayContext;
import org.yyh.core.filter.Filter;
import org.yyh.core.filter.FilterAspect;
import org.yyh.core.helper.ResponseHelper;
import org.yyh.core.response.GatewayResponse;
import static org.yyh.common.constants.FilterConst.*;
import java.util.Map;


@Slf4j
@FilterAspect(id=MOCK_FILTER_ID,
        name = MOCK_FILTER_NAME,
        order = MOCK_FILTER_ORDER)
public class MockFilter implements Filter {
    @Override
    public void doFilter(GatewayContext ctx) throws Exception {
        Rule.FilterConfig config = ctx.getRule().getFilterConfig(MOCK_FILTER_ID);
        if (config == null) {
            return;
        }

        Map<String, String> map = JSONUtil.parse(config.getConfig(), Map.class);
        String value = map.get(ctx.getRequest().getMethod().name() + " " + ctx.getRequest().getPath());
        if (value != null) {
            ctx.setResponse(GatewayResponse.buildGatewayResponse(value));
            ctx.written();
            ResponseHelper.writeResponse(ctx);
            log.info("mock {} {} {}", ctx.getRequest().getMethod(), ctx.getRequest().getPath(), value);
            ctx.terminated();
        }
    }
}
