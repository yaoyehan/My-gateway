package org.yyh.core.filter.user;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yyh.common.enums.ResponseCode;
import org.yyh.common.exception.ResponseException;
import org.yyh.core.context.GatewayContext;
import org.yyh.core.filter.Filter;
import org.yyh.core.filter.FilterAspect;

import static org.yyh.common.constants.FilterConst.*;

@Slf4j
@FilterAspect(id= USER_AUTH_FILTER_ID,
        name = USER_AUTH_FILTER_NAME,
        order =USER_AUTH_FILTER_ORDER )
public class UserAuthFilter implements Filter {
    private static final String SECRET_KEY = "faewifheafewhefsfjkds";
    private static final String COOKIE_NAME = "user-jwt";

    @Override
    public void doFilter(GatewayContext ctx) throws Exception {
        //检查是否需要用户鉴权
        if (ctx.getRule().getFilterConfig(USER_AUTH_FILTER_ID) == null) {
            return;
        }

        String token = ctx.getRequest().getCookie(COOKIE_NAME).value();
        if (StringUtils.isBlank(token)) {
            throw new ResponseException(ResponseCode.UNAUTHORIZED);
        }

        try {
            //解析用户id
            long userId = parseUserId(token);
            //把用户id传给下游
            ctx.getRequest().setUserId(userId);
        } catch (Exception e) {
            throw new ResponseException(ResponseCode.UNAUTHORIZED);
        }

    }

    private long parseUserId(String token) {
        Jwt jwt = Jwts.parser().setSigningKey(SECRET_KEY).parse(token);
        return Long.parseLong(((DefaultClaims)jwt.getBody()).getSubject());
    }
}
