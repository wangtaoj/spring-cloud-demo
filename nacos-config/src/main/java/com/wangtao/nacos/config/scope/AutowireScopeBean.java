package com.wangtao.nacos.config.scope;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangtao
 * Created at 2023-10-28
 */
@Getter
@Component
public class AutowireScopeBean {

    @Autowired
    private ScopeBean scopeBean;

}
