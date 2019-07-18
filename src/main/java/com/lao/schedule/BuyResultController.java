package com.lao.schedule;

import com.lao.util.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    @RequestMapping("/logout")
    public List<MsisdnDto> logout() {
        List<MsisdnDto> msisdnList = ScheduleConfig.msisdnList;
        ScheduleConfig.msisdnList = new ArrayList<>();
        return msisdnList;
    }
    @RequestMapping("/login")
    public List<MsisdnDto> login() {
        List<String> list = new ArrayList<>();
        list.add("18148601205");
        list.add("17722859084");
        list.add("17520089084");
        list.add("16605110602");
        list.add("18124276537");
        List<MsisdnDto> result = new ArrayList<>();
        scheduleConfig.keepLive();
        Set<String> set = new HashSet<>();
        for (MsisdnDto dto : ScheduleConfig.msisdnList) {
            set.add(dto.getMsisdn());
        }
        for (String s : list) {
            try {
                if (set.contains(s)) continue;
                result.add(LoginUtils.login(s));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ScheduleConfig.msisdnList.addAll(result);
        return ScheduleConfig.msisdnList;
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
    public String buyResultKey() {
        StringBuilder ss = new StringBuilder();
        for (String dto : map.keySet()) {
            ss.append(dto).append("------").append("\n");
        }
        return ss.toString();
    }
}
