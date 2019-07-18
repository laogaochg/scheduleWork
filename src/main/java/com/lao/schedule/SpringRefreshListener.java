package com.lao.schedule;

import java.io.File;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
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
        //验证码图片存储地址
        File imageFile = new File(environment.getProperty("imgPatch"));
        Tesseract tessreact = new Tesseract();
        tessreact.setDatapath(environment.getProperty("dataPatch"));
        tessreact.setLanguage("eng");//选择字库文件（只需要文件名，不需要后缀名）

        String result;
        try {
            result = "测验结果：" + tessreact.doOCR(imageFile);
            logger.info(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
