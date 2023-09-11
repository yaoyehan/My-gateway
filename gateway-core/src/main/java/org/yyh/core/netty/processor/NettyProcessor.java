package org.yyh.core.netty.processor;

import org.yyh.core.context.HttpRequestWrapper;

public interface NettyProcessor {

    void process(HttpRequestWrapper wrapper);
}
