package com.lao.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lao.util.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
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

    @RequestMapping("/buy")
    public List<MsisdnDto> buy() {
        for (String s : ScheduleConfig.animal.keySet()) {
            scheduleConfig.buyList(s);
        }
        return ScheduleConfig.msisdnList;
    }

    @RequestMapping("/loginB")
    public List<MsisdnDto> loginB() {
        readFile();
        scheduleConfig.keepLive();
        return ScheduleConfig.msisdnList;
    }

    public void readFile() {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(new File(environment.getProperty("filePath"))));
            List<MsisdnDto> now = new ArrayList<>();
            while (bf.ready()) {
                String line = bf.readLine().trim();
                if (StringUtils.hasText(line)) {
                    String[] split = line.split("\\|");
                    MsisdnDto dto1 = new MsisdnDto();
                    dto1.setMsisdn(split[0]);
                    dto1.setBuyIds("");
                    dto1.setCookie("");
                    dto1.setLuckKey(split[1]);
                    now.add(dto1);
                }

            }
            ScheduleConfig.msisdnList = now;
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @RequestMapping("/totalMoney")
    public String totalMoney() {
        BigDecimal totalIncome = new BigDecimal(0);
        BigDecimal totalAsset = new BigDecimal(0);
        for (MsisdnDto m : ScheduleConfig.msisdnList) {
            String s = scheduleConfig.post(environment.getProperty("findUserAssets"), "{}", m.getCookie(), m.getLuckKey());
            JSONObject jsonObject = JSON.parseObject(s);
            //收益
            totalIncome = totalIncome.add(jsonObject.getJSONObject("data").getBigDecimal("totalIncome"));
            totalAsset = totalAsset.add(jsonObject.getJSONObject("data").getBigDecimal("totalAsset"));
        }
        BigDecimal a = totalAsset.subtract(totalIncome);
        return "资产:" + totalAsset + "，收益:" + totalIncome + "，本金:" + a;
    }
}
