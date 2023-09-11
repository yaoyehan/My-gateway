package org.yyh.gateway.config.center.api;

import org.yyh.common.config.Rule;

import java.util.List;

public interface RulesChangeListener {
    void onRulesChange(List<Rule> rules);
}
