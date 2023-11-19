package com.wangtao.nacos.config;

import com.wangtao.nacos.config.scope.AutowireScopeBean;
import com.wangtao.nacos.config.scope.ScopeBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author wangtao
 * Created at 2023-10-28
 */
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private ConfigurableApplicationContext ac;

    @Autowired
    private ContextRefresher contextRefresher;

    @Autowired
    private AutowireScopeBean autowireScopeBean;

    @Test
    public void contextLoad() {

        String[] beanNames = ac.getBeanNamesForType(ScopeBean.class);
        Assertions.assertEquals("scopedTarget.scopeBean", beanNames[0]);
        Assertions.assertEquals("scopeBean", beanNames[1]);

        // 代理bean
        ScopeBean beanBefore = ac.getBean(ScopeBean.class);
        System.out.println(beanBefore.getClass());
        System.out.println(beanBefore.getClass().getSuperclass());
        // 目标bean
        ScopeBean targetBeanBefore1 = (ScopeBean) ac.getBean("scopedTarget.scopeBean");
        ScopeBean targetBeanBefore2 = (ScopeBean) ac.getBean("scopedTarget.scopeBean");
        Assertions.assertSame(targetBeanBefore1, targetBeanBefore2);
        // 刷新
        contextRefresher.refresh();
        ScopeBean beanAfter = ac.getBean(ScopeBean.class);
        Assertions.assertSame(beanBefore, beanAfter);
        Assertions.assertSame(beanBefore, autowireScopeBean.getScopeBean());
        ScopeBean targetBeanAfter = (ScopeBean) ac.getBean("scopedTarget.scopeBean");
        Assertions.assertNotSame(targetBeanBefore1, targetBeanAfter);

    }
}
