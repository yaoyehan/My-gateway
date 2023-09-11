package org.yyh.gateway.register.center.api;

import org.yyh.common.config.ServiceDefinition;
import org.yyh.common.config.ServiceInstance;

import java.util.Set;

public interface RegisterCenterListener {

    void onChange(ServiceDefinition serviceDefinition,
                  Set<ServiceInstance> serviceInstanceSet);
}
