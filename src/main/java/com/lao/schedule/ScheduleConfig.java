package com.lao.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: LaoGaoChuang
 * @Date : 2019/7/12 20:16
 */
@Component
public class ScheduleConfig {
    private static Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);
    public volatile static List<MsisdnDto> msisdnList = new ArrayList<>();

    @Autowired
    private Environment environment;
    public static Map<String, String> animal = new HashMap<>();

    static {
        animal.put("2", "鸡");
        animal.put("5", "羊");
        animal.put("1", "鼠");
        animal.put("6", "猪");
        animal.put("17", "马");
        animal.put("4", "狗");
        animal.put("18", "牛");
        animal.put("3", "兔");
    }




//    @Scheduled(cron = "${buy2}")
    public void buy2() {
        buyList("2");//鸡
    }


//    @Scheduled(cron = "${buy6}")
    public void buy6() {
        buyList("6"); //猪
    }

//    @Scheduled(cron = "${buy17}")
    public void buy17() {
        buyList("17");//马
    }


    @Scheduled(cron = "${buy3}")
    public void buy3() {
        buyList("3");//兔
    }


    @Scheduled(cron = "${buy5}")
    public void buy5() {
        buyList("5"); //羊
    }

//    @Scheduled(cron = "${buy18}")
    public void buy18() {
        buyList("18"); //牛
    }

    @Scheduled(cron = "${findAdopedtListCron}")
    public void findAdopedtList() {
        //已经领养的纪录
        for (MsisdnDto dto : msisdnList) {
            ((Runnable) () -> {
                String s = post(environment.getProperty("findAdopedtList"), "{\"pageNo\":1,\"pageSize\":10}", dto.getCookie(), dto.getLuckKey());
                logger.info("手机号码:{}领养纪录:{}", dto.getMsisdn(), s);
            }
            ).run();
        }
    }

//    @Scheduled(cron = "${keepLive}")
    public void keepLive() {
//        readFile();
        List<MsisdnDto> list = new ArrayList<>();
        for (MsisdnDto dto : msisdnList) {
            try {
                String url = environment.getProperty("checkUrl");
                String s = post(url, "{}", dto.getCookie(), dto.getLuckKey());
                logger.info("{}.心跳:{}", dto.getMsisdn(), s);
                if (s.contains("\"code\":200")) {
                    list.add(dto);
                } else {
                    logger.error("{}.心跳出错:{}", dto.getMsisdn(), s);
                }
            } catch (NestedRuntimeException e) {
                logger.error("{}.心跳出错:{}", dto.getMsisdn(), e.getStackTrace()[0]);
            }
        }
        msisdnList = list;
    }


    public void buyList(String id) {
        try {
            buy(id);
            Thread.sleep(1000);
            buy(id);
            Thread.sleep(1000);
            buy(id);
            Thread.sleep(5000);
            buy(id);
            Thread.sleep(5000);
            buy(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);


    private void buy(String id) {
        for (MsisdnDto dto : msisdnList) {
            logger.info("{}请求买{}", dto.getMsisdn(), animal.get(id));
            String url = environment.getProperty("flashBuyUrl") + id;
            String body = "{\"id\":\"" + id + "\"}";
            Runnable r = () -> {
                String key = dto.getMsisdn() + "买了" + animal.get(id);
                if(BuyResultController.map.containsKey(key)){
                    return;
                }
                String buy = post(url, body, dto.getCookie(), dto.getLuckKey());
                String s = String.format("%s 抢 %s 结果：%s", dto.getMsisdn(), animal.get(id), buy);
                if (s.contains("成功")) {
                    BuyResultController.map.put(key, s);
                }
                logger.info(s);
            };
            executor.execute(r);
        }
    }




    public String post(String url, String body, String cookie, String luckkey) {
        try {
            return (new RestTemplate()).postForObject(url, new HttpEntity(body, this.getHeaders(cookie, luckkey)), String.class, new Object[0]);
        } catch (HttpClientErrorException e) {
            logger.info("请求出错:{}", e.getResponseBodyAsString());
            return e.getResponseBodyAsString();
        }
    }

    public String get(String url, String cookie, String luckkey) {
        RestTemplate restTemplate = new RestTemplate();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(this.getHttpEntity(null, cookie, luckkey), String.class);
        ResponseExtractor responseExtractor = restTemplate.responseEntityExtractor(String.class);

        try {
            ResponseEntity<String> response = (ResponseEntity) restTemplate.execute(url, HttpMethod.GET, requestCallback, responseExtractor, new Object[0]);
            return response.getBody();
        } catch (HttpStatusCodeException var8) {
            return var8.getResponseBodyAsString();
        }
    }

    public HttpEntity getHttpEntity(String body, String cookie, String luckkey) {
        return body == null ? new HttpEntity(this.getHeaders(cookie, luckkey)) : new HttpEntity(body, this.getHeaders(cookie, luckkey));
    }

    private HttpHeaders getHeaders(String cookie, String luckkey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        headers.add("luckkey", luckkey);
        headers.add("Referer", "http://sgkgdmn.singaporeluckyzodiac.com/");
        headers.add("Host", "sgkgdmn.singaporeluckyzodiac.com");
        headers.add("Content-Type", "application/json;charset=UTF-8");
        headers.add("Accept", "*/*");
        headers.add("Accept-Encoding", "UTF-8");
        headers.add("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        return headers;
    }

}
