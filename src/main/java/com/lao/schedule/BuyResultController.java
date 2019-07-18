package com.lao.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: LaoGaoChuang
 * @Date : 2019/7/13 13:46
 */
@RestController
public class BuyResultController {
    public static Map<String, String> map = new HashMap<>();
    @Autowired
    private ScheduleConfig scheduleConfig;
    @Autowired
    private Environment environment;

    @RequestMapping("/buyResult")
    public Map<String, String> result() {
        return map;
    }

    @RequestMapping("/findAdopedtList")
    public String findAdopedtList() {
        StringBuilder ss = new StringBuilder();
        for (MsisdnDto dto : ScheduleConfig.msisdnList) {
            String s = scheduleConfig.post(environment.getProperty("findAdoptList"), "{\"pageNo\":1,\"pageSize\":10}", dto.getCookie(), dto.getLuckKey());
            ss.append(dto.getMsisdn()).append("-->").append(s).append("\n");
        }
        return ss.toString();
    }

    @RequestMapping("/buyResultKey")
    public Set<String> buyResultKey() {
        return map.keySet();
    }
}
