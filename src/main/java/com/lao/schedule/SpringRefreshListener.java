package com.lao.schedule;

import com.lao.util.LoginUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * laogaochg
 * 2017/7/6.
 */
@Component
public class SpringRefreshListener implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger logger = LoggerFactory.getLogger(SpringRefreshListener.class);
    @Autowired
    private Environment environment;
    public static ApplicationContext context;

    /**
     * springMVC启动加载定时任务
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        context = event.getApplicationContext();
        LoginUtils.ENVIRONMENT = environment;
    }

}
